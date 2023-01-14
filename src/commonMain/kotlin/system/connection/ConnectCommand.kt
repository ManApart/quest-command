package system.connection

import core.Player
import core.commands.Command
import core.commands.removeAll
import core.events.EventManager
import traveling.direction.Direction

class ConnectCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Connect", "Connection")
    }

    override fun getDescription(): String {
        return "Connect to a remote server."
    }

    override fun getManual(): String {
        return """  
	Connect - View your current connection information.
	Connect <playerName> <host> *<port> - Connect to a given host and port. Defaults to localhost and 8080
    If the playerName matches no players on the server, a new one will be created
    """
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> listOf("Player")
            args.size == 1 -> listOf("localhost")
            args.size == 2 -> listOf("8080")
            else -> listOf()
        }
    }

    override suspend fun execute(source: Player, keyword: String, args: List<String>) {
        if (args.isEmpty()) {
            EventManager.postEvent(ConnectInfoEvent(source))
        } else {
            val parsed = parseConnectArgs(args)
            EventManager.postEvent(ConnectEvent(source, parsed.playerName, parsed.hostName, parsed.port))
        }
    }

    private fun parseConnectArgs(args: List<String>): ConnectionInfo {
        val port = args.firstOrNull { it.toIntOrNull() != null } ?: "8080"
        val host = findHost(args)
        val name = findName(args, host, port)
        return ConnectionInfo(name, host, port)
    }

    private fun findHost(args: List<String>): String {
        val host = if (args.size == 3) args[1] else args.firstOrNull { it.contains(".") } ?: "localhost"
        return if (host.startsWith("http")) host else "http://$host"
    }

    private fun findName(args: List<String>, host: String, port: String): String {
        val unused = args.removeAll(listOf(host, host.replace("http://", ""), port))
        return if (unused.isNotEmpty()) unused.first() else "Player"
    }



}

private data class ConnectionInfo(val playerName: String, val hostName: String, val port: String)
