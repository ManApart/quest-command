package system.connection

import addHistoryMessageAfterCallback
import core.history.GameLogger
import core.history.TerminalPrinter
import core.history.displayGlobal
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.js.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*


 object WebClient {
    private val client by lazy { buildWebClient() }
     var doPolling = false
     var host = "http://localhost"
     var port = "8080"
     var latestResponse = 0
     var latestSubResponse = 0
     var playerName = "Player"
     var latestInfo = ServerInfo()

    private fun buildWebClient(): HttpClient {
        return HttpClient(Js) { install(ContentNegotiation) { json() } }
    }


     fun createServerConnectionIfPossible(host: String, port: String, playerName: String, callback: (ServerInfo) -> Unit) {
        getServerInfo(host, port) { info ->
            latestInfo = info
            if (latestInfo.validServer) {
                this.host = host
                this.port = port
                this.playerName = playerName
                if (latestInfo.playerNames.none { it.lowercase() == playerName.lowercase() }) {
                    GlobalScope.launch {
                        latestInfo = createPlayer(playerName)
                        callback(latestInfo)
                    }
                } else {
                    callback(latestInfo)
                }
            } else {
                callback(latestInfo)
            }
        }
    }

     fun getServerInfo(host: String = this.host, port: String = this.port, callback: (ServerInfo) -> Unit) {
        latestInfo = ServerInfo()
        GlobalScope.launch {
            try {
                latestInfo = client.get("$host:$port/info").body()
            } catch (e: Exception) {
                ServerInfo()
            }
            callback(latestInfo)
        }
    }

    private suspend fun createPlayer(name: String): ServerInfo {
        return try {
            client.post("$host:$port/$name").body()
        } catch (e: Exception) {
            ServerInfo()
        }
    }

     fun sendCommand(line: String, callback: (List<String>) -> Unit) {
        GlobalScope.launch {
            val responses = try {
                val response: ServerResponse = client.post("$host:$port/$playerName/command") {
                    parameter("start", latestResponse)
                    parameter("startSub", latestSubResponse)
                    setBody(line)
                }.body()

                this@WebClient.latestResponse = response.latestResponse
                this@WebClient.latestSubResponse = response.latestSubResponse
                response.history
            } catch (e: Exception) {
                listOf("Unable to hit server.")
            }
            callback(responses)
        }
    }

     fun pollForUpdates() {
        doPolling = true
        GlobalScope.launch {
            while (doPolling) {
                if (latestInfo.validServer) {
                    val updates = getServerUpdates()
                    if (updates.isNotEmpty() && !updates.first().startsWith("No history for")) {
                        updates.forEach { addHistoryMessageAfterCallback(it) }
                    }
                    delay(1000)
                }
            }
        }
    }

     fun getServerHistory(callback: (List<String>) -> Unit) {
        GlobalScope.launch {
            callback(getServerUpdates())
        }
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
