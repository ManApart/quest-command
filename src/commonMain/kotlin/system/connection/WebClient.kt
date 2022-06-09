package system.connection

import kotlinx.serialization.Serializable

@Serializable
data class ServerInfo(val gameName: String = "Game", val playerNames: List<String> = listOf(), val validServer: Boolean = false) {
    override fun toString(): String {
        return if (validServer) "$gameName with players ${playerNames.joinToString()}" else "Invalid Server"
    }
}

@Serializable
data class ServerResponse(val latestResponse: Int, val latestSubResponse: Int, val history: List<String>)

expect object WebClient {
    var doPolling: Boolean
    var host: String
    var port: String
    var latestResponse: Int
    var latestSubResponse: Int
    var playerName: String
    var latestInfo: ServerInfo

    fun createServerConnectionIfPossible(host: String, port: String, playerName: String, callback: (ServerInfo) -> Unit)
    fun getServerInfo(host: String = this.host, port: String = this.port, callback: (ServerInfo) -> Unit)
    fun sendCommand(line: String, callback: (List<String>) -> Unit)
    fun pollForUpdates()
    fun getServerHistory(callback: (List<String>) -> Unit)

}