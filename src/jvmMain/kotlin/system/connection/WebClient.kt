package system.connection

import core.history.GameLogger
import core.history.TerminalPrinter
import core.history.displayGlobal
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

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
        return HttpClient(CIO) { install(ContentNegotiation) { json() } }
    }

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

    fun getSuggestions(line: String, callback: (List<String>) -> Unit) {
        try {
            val suggestions: List<String> = runBlocking {
                client.post("$host:$port/$playerName/suggestion") {
                    setBody(line)
                }.body()
            }
            callback(suggestions)
        } catch (e: Exception) {
            callback(listOf())
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

    @OptIn(DelicateCoroutinesApi::class)
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
