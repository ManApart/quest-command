package system.connection

import addHistoryMessageAfterCallback
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

    suspend fun createServerConnectionIfPossible(host: String, port: String, playerName: String): ServerInfo {
        latestInfo = getServerInfo(host, port)
        return if (latestInfo.validServer) {
            this.host = host
            this.port = port
            this.playerName = playerName
            if (latestInfo.playerNames.none { it.lowercase() == playerName.lowercase() }) {
                createPlayer(playerName)
            } else {
                latestInfo
            }
        } else {
            latestInfo
        }
    }

    suspend fun getServerInfo(host: String = this.host, port: String = this.port): ServerInfo {
        return try {
            client.get("$host:$port/info").body()
        } catch (e: Exception) {
            ServerInfo()
        }
    }

    private suspend fun createPlayer(name: String): ServerInfo {
        return try {
            client.post("$host:$port/$name").body()
        } catch (e: Exception) {
            ServerInfo()
        }
    }

    suspend fun getSuggestions(line: String): List<String> {
        return try {
            client.post("$host:$port/$playerName/suggestion") {
                setBody(line)
            }.body()
        } catch (e: Exception) {
            listOf()
        }
    }

    suspend fun sendCommand(line: String): List<String> {
        return try {
            val response: ServerResponse = client.post("$host:$port/$playerName/command") {
                parameter("start", latestResponse)
                parameter("startSub", latestSubResponse)
                setBody(line)
            }.body()

            latestResponse = response.latestResponse
            latestSubResponse = response.latestSubResponse
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server.")
        }
    }

    suspend fun pollForUpdates() {
        doPolling = true
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


    suspend fun getServerUpdates(): List<String> {
        return try {
            val response: ServerResponse = client.get("$host:$port/$playerName/history") {
                parameter("start", latestResponse)
                parameter("startSub", latestSubResponse)
            }.body()

            latestResponse = response.latestResponse
            latestSubResponse = response.latestSubResponse
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server")
        }
    }
}
