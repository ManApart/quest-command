package system.connection

import core.Player
import core.commands.CommandInterceptor
import core.history.displayToMe

class ConnectionCommandInterceptor : CommandInterceptor {
    override fun ignoredCommands(): List<String> {
        return listOf("Disconnect", "Connect")
    }

    override fun parseCommand(source: Player, line: String) {
        WebClient.sendCommand(source.id, line).forEach { source.displayToMe(it) }
    }

}