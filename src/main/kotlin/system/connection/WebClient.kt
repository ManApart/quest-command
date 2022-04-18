package system.connection

import core.GameState
import core.POLL_CONNECTION
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

data class ServerInfo(val gameName: String = "Game", val userId: Int = 0, val validServer: Boolean = false)
data class ServerResponse(val lastResponse: Int, val history: List<String>)

object WebClient {
    var actve = false
    var host = "localhost"
    var port = "8080"
    var lastResponse = 0
    var playerId = 0
    private val client by lazy { HttpClient.newBuilder().build() }

    fun createServerIfPossible(host: String, port: String): ServerInfo {
        val info = getServerInfo(host, port)
        if (info.validServer){
            this.host = host
            this.port = port
            this.playerId = getNewPlayerId()
            if (GameState.properties.values.getBoolean(POLL_CONNECTION)) pollForUpdates()
        }
        return info
    }

    fun getServerInfo(host: String = this.host, port: String = this.port): ServerInfo {
        val request = HttpRequest.newBuilder()
            .uri(URI.create("$host:$port"))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        return ServerInfo()
    }

    fun getNewPlayerId(): Int {
        "$host:$port/playerId"
        return 0
    }

    fun sendCommand(line: String): List<String> {
        try {
            val request = HttpRequest.newBuilder()
                .uri(URI.create("$host:$port/command/$playerId"))
//                .withBody(line)
                .build()
            //Use Ktor to be multiplatform
            val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        } catch (e: Exception) {
            return listOf("Unable to hit server")
        }
        return listOf()
    }

    fun getServerUpdates() {
        "$host:$port/updates/$playerId/$lastResponse"
        println("Get Updates")
    }

    private fun pollForUpdates() {
        GlobalScope.launch {
            while (true) {
                async {
                    getServerUpdates()
                    delay(1000)
                }.await()
            }
        }
    }

}