package system.connection

import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe
import system.connection.WebClient.port

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
        if (args.isEmpty()) {
            EventManager.postEvent(ConnectInfoEvent(source))
        } else {
            val parsed = parseConnectArgs(args)
            EventManager.postEvent(ConnectEvent(source, parsed.playerName, parsed.hostName, parsed.port))
        }
    }

    private fun parseConnectArgs(args: List<String>): ConnectionInfo {
        val name = args.first()
        val port = if (args.last().toIntOrNull() != null) args.last() else "8080"
        val host = if (args.size > 1) args[1] else "localhost"
        val cleanHost = if (host.startsWith("http")) host else "http://$host"
        return ConnectionInfo(name, cleanHost, port)
    }

}

private data class ConnectionInfo(val playerName: String, val hostName: String, val port: String)