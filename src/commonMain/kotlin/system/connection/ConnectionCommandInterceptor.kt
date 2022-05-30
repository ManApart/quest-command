package system.connection

import core.Player
import core.commands.CommandInterceptor
import core.history.displayToMe

class ConnectionCommandInterceptor : CommandInterceptor {
    override fun ignoredCommands(): List<String> {
        return listOf("Disconnect", "Connect", "Play", "Be", "Quit")
    }

    override fun parseCommand(source: Player, line: String) {
        WebClient.sendCommand(line).forEach {
            source.displayToMe(it)
        }
    }

}