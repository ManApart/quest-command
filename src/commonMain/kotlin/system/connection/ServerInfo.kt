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