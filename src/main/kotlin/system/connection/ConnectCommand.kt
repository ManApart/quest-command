package system.connection

import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe

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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        when {
            args.isEmpty() -> EventManager.postEvent(ConnectInfoEvent(source))
            args.size >= 2 -> {
                val parsed = parseConnectArgs(args)
                EventManager.postEvent(ConnectEvent(source, parsed.playerName, parsed.hostName, parsed.port))
            }
            else -> source.displayToMe("Could not understand args ${args.joinToString(" ")}")
        }
    }

    private fun parseConnectArgs(args: List<String>): ConnectionInfo {
        val port = if (args.last().toIntOrNull() != null) args.last() else ""
        val host = if (port.isBlank()) args.last() else args[args.size - 2]
        val name = args.joinToString(" ").replace(port, "").replace(host, "")
        return ConnectionInfo(name, host, port)
    }

}

private data class ConnectionInfo(val playerName: String, val hostName: String, val port: String)
