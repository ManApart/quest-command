package system.connection

import addHistoryMessageAfterCallback
import core.Player
import core.commands.CommandInterceptor

actual class ConnectionCommandInterceptor : CommandInterceptor {
    actual override fun ignoredCommands(): List<String> {
        return ignoredConnectionCommands
    }

    actual override suspend fun parseCommand(source: Player, line: String) {
        WebClient.sendCommand(line).forEach {
            addHistoryMessageAfterCallback(it)
        }
    }

}