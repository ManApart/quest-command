package system.connection

import core.GameState
import core.POLL_CONNECTION
import core.history.GameLogger
import core.history.TerminalPrinter
import core.history.display
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class ServerInfo(val gameName: String = "Game", val playerNames: List<String> = listOf(), val validServer: Boolean = false)

@Serializable
data class ServerResponse(val lastResponse: Int, val history: List<String>)

object WebClient {
    private val client by lazy { HttpClient(CIO) { install(ContentNegotiation) { json() } } }
    var doPolling = false
    var host = "localhost"
    var port = "8080"
    var latestResponse = 0
    var playerName = "Player"
    var latestInfo = ServerInfo()

    fun createServerConnectionIfPossible(host: String, port: String, playerName: String): ServerInfo {
        latestInfo = getServerInfo(host, port)
        if (latestInfo.validServer) {
            this.host = host
            this.port = port
            this.playerName = playerName
            if (latestInfo.playerNames.none { it.lowercase() == playerName.lowercase() }) {
                latestInfo = createPlayer(playerName)
            }
            if (GameState.properties.values.getBoolean(POLL_CONNECTION)) pollForUpdates()
        }
        return latestInfo
    }

    fun getServerInfo(host: String = this.host, port: String = this.port): ServerInfo {
        return try {
            runBlocking { client.get("$host:$port/info").body() }
        } catch (e: Exception) {
            ServerInfo()
        }.also {
            this.latestInfo = it
        }
    }

    private fun createPlayer(name: String): ServerInfo {
        return try {
            runBlocking { client.post("$host:$port/$name").body() }
        } catch (e: Exception) {
            ServerInfo()
        }
    }

    fun sendCommand(line: String): List<String> {
        return try {
            val response: ServerResponse = runBlocking {
                client.post("$host:$port/$playerName/command") {
                    parameter("start", latestResponse + 1)
                    setBody(line)
                }.body()
            }
            synchronized(this) {
                this@WebClient.latestResponse = response.lastResponse
            }
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server.")
        }
    }

    private fun pollForUpdates() {
        doPolling = true
        GlobalScope.launch {
            while (doPolling) {
                if (latestInfo.validServer) {
                    runBlocking {
                        launch {
                            val updates = getServerUpdates()
                            if (updates.isNotEmpty()) {
                                updates.forEach { display(it) }
                                GameLogger.endCurrent()
                                TerminalPrinter.print()
                            }
                        }
                        delay(1000)
                    }
                }
            }
        }
    }

    fun getServerHistory(): List<String> {
        return runBlocking { getServerUpdates() }
    }

    private suspend fun getServerUpdates(): List<String> {
        return try {
            val response: ServerResponse = client.get("$host:$port/$playerName/history") {
                parameter("start", latestResponse)
            }.body()

            synchronized(this) {
                this@WebClient.latestResponse = response.lastResponse
            }
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server")
        }
    }

}