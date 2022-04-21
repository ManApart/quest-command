package system.connection

import core.GameState
import core.POLL_CONNECTION
import core.history.displayToMe
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
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
            if (latestInfo.playerNames.none{ it.lowercase() == playerName.lowercase()}){
                sendCommand("create $playerName")
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

    fun sendCommand(line: String): List<String> {
        return try {
            val response: ServerResponse = runBlocking {
                client.post("$host:$port/$playerName/command") {
                    setBody(line)
                }.body()
            }
            this@WebClient.latestResponse = response.lastResponse
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
                            updates.forEach { GameState.player.displayToMe(it) }
                        }
                        delay(1000)
                    }
                }
            }
        }
    }

    private suspend fun getServerUpdates(): List<String> {
        return try {
            val response: ServerResponse = client.get("$host:$port/$playerName/history") {
                parameter("since", latestResponse)
            }.body()

            this@WebClient.latestResponse = response.lastResponse
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server")
        }
    }

}