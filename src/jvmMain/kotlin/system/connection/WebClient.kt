package system.connection

import core.history.GameLogger
import core.history.TerminalPrinter
import core.history.displayGlobal
import core.utility.buildWebClient
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

actual object WebClient {
    private val client by lazy { buildWebClient() }
    actual var doPolling = false
    actual var host = "http://localhost"
    actual var port = "8080"
    actual var latestResponse = 0
    actual var latestSubResponse = 0
    actual var playerName = "Player"
    actual var latestInfo = ServerInfo()

    private fun buildWebClient(): HttpClient {
        return HttpClient(CIO) { install(ContentNegotiation) { json() } }
    }


    actual fun createServerConnectionIfPossible(host: String, port: String, playerName: String): ServerInfo {
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

    actual fun getServerInfo(host: String, port: String): ServerInfo {
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

    actual fun sendCommand(line: String): List<String> {
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
    actual fun pollForUpdates() {
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

    actual fun getServerHistory(): List<String> {
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