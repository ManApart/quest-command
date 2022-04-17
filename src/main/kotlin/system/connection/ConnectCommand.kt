package system.connection

import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.Player
import core.commands.Command
import core.events.EventManager
import core.history.displayToMe
import core.utility.capitalize2

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
	Connect <host> *<port> - Connect to a given host and port. Defaults to localhost and 8080
    """
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        when {
            args.isEmpty() -> EventManager.postEvent(ConnectInfoEvent(source))
            args.size == 1 -> EventManager.postEvent(ConnectEvent(source, args.first()))
            args.size == 2 && args.last().toIntOrNull() != null -> EventManager.postEvent(ConnectEvent(source, args.first(), args.last()))
            else -> source.displayToMe("Could not understand args ${args.joinToString(" ")}")
        }

    }
}
