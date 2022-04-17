package system.connection

import core.Player
import core.commands.Command
import core.events.EventManager

class DisconnectCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Disconnect")
    }

    override fun getDescription(): String {
        return "Disconnect from a remote server."
    }

    override fun getManual(): String {
        return "Disconnect - Disconnect from a remote server."
    }

    override fun getCategory(): List<String> {
        return listOf("System")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        EventManager.postEvent(ConnectEvent(source))
    }
}
