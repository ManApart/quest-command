package system.connection

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


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
        return HttpClient { install(ContentNegotiation) { json() } }
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
//        GlobalScope.launch(Dispatchers.Default) {
//            try {
//                client.get("$host:$port/info").body()
//            } catch (e: Exception) {
//                ServerInfo()
//            }.also {
//                this.latestInfo = it
//            }
//        }
        throw NotImplementedError()
    }

    private fun createPlayer(name: String): ServerInfo {
        throw NotImplementedError()
    }

    actual fun sendCommand(line: String): List<String> {
        throw NotImplementedError()
    }

    //Currently not working correctly
    actual fun pollForUpdates() {
        throw NotImplementedError()
    }

    actual fun getServerHistory(): List<String> {
        throw NotImplementedError()
    }

}