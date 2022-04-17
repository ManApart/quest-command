package system.connection

//TODO - how do we poll for updates?
object WebClient {
    var host = "localhost"
    var port = "8080"


    fun getServerInfo(): String {
        return "$host:$port"
    }

    fun isValidServer(host: String, port: String): Boolean{
        return false
    }

    fun sendCommand(playerId: Int, line: String): List<String> {
        return listOf()
    }
}