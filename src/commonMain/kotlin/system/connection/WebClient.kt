package system.connection

import core.history.GameLogger
import core.history.TerminalPrinter
import core.history.displayGlobal
import core.utility.buildWebClient
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable

@Serializable
data class ServerInfo(val gameName: String = "Game", val playerNames: List<String> = listOf(), val validServer: Boolean = false){
    override fun toString(): String {
        return if (validServer) "$gameName with players ${playerNames.joinToString()}" else "Invalid Server"
     }
}

@Serializable
data class ServerResponse(val latestResponse: Int, val latestSubResponse: Int, val history: List<String>)

object WebClient {
    private val client by lazy { buildWebClient() }
    var doPolling = false
    var host = "http://localhost"
    var port = "8080"
    var latestResponse = 0
    var latestSubResponse = 0
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
                    parameter("start", latestResponse)
                    parameter("startSub", latestSubResponse)
                    setBody(line)
                }.body()
            }
            this@WebClient.latestResponse = response.latestResponse
            this@WebClient.latestSubResponse = response.latestSubResponse
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server.")
        }
    }

    //Currently not working correctly
    fun pollForUpdates() {
        doPolling = true
        GlobalScope.launch {
            while (doPolling) {
                if (latestInfo.validServer) {
                    runBlocking {
                        launch {
                            val updates = getServerUpdates()
                            if (updates.isNotEmpty() && !updates.first().startsWith("No history for")) {
                                updates.forEach { displayGlobal(it) }
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
                parameter("startSub", latestSubResponse)
            }.body()

            this@WebClient.latestResponse = response.latestResponse
            this@WebClient.latestSubResponse = response.latestSubResponse
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server")
        }
    }

}