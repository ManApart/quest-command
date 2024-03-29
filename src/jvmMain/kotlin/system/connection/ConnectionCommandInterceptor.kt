package system.connection

import core.Player
import core.commands.CommandInterceptor
import core.history.displayToMe

actual class ConnectionCommandInterceptor : CommandInterceptor {
    actual override fun ignoredCommands(): List<String> {
        return ignoredConnectionCommands
    }

    actual override suspend fun parseCommand(source: Player, line: String) {
        WebClient.sendCommand(line).forEach {
            source.displayToMe(it)
        }
    }

}