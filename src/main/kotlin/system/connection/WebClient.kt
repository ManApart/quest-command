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

data class ServerInfo(val gameName: String = "Game", val userId: Int = 0, val validServer: Boolean = false)
data class ServerResponse(val lastResponse: Int, val history: List<String>)

object WebClient {
    private val client by lazy { HttpClient(CIO) { install(ContentNegotiation) { json() } } }
    var host = "localhost"
    var port = "8080"
    var latestResponse = 0
    var playerId = 0
    val latestInfo = ServerInfo()

    fun createServerIfPossible(host: String, port: String): ServerInfo {
        val info = getServerInfo(host, port)
        if (info.validServer) {
            this.host = host
            this.port = port
            this.playerId = getNewPlayerId()
            if (GameState.properties.values.getBoolean(POLL_CONNECTION)) pollForUpdates()
        }
        return info
    }

    fun getServerInfo(host: String = this.host, port: String = this.port): ServerInfo {
        return try {
            runBlocking { client.get("$host:$port/info").body() }
        } catch (e: Exception) {
            ServerInfo()
        }
    }

    fun getNewPlayerId(): Int {
        return try {
            runBlocking { client.get("$host:$port/playerId").body() }
        } catch (e: Exception) {
            0
        }
    }

    fun sendCommand(line: String): List<String> {
        return try {
            val response: ServerResponse = runBlocking {
                client.post("$host:$port/$playerId/command") {
                    setBody(line)
                }.body()
            }
            this@WebClient.latestResponse = response.lastResponse
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun pollForUpdates() {
        GlobalScope.launch {
            while (true) {
//                async {
                val updates = getServerUpdates()
                updates.forEach { GameState.player.displayToMe(it) }
                delay(1000)
//                }.await()
            }
        }
    }

    private suspend fun getServerUpdates(): List<String> {
        return try {
            val response: ServerResponse = client.get("$host:$port/updates/$playerId") {
                parameter("since", latestResponse)
            }.body()

            this@WebClient.latestResponse = response.lastResponse
            response.history
        } catch (e: Exception) {
            listOf("Unable to hit server")
        }
    }

}