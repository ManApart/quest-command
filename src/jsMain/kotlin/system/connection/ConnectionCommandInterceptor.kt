package system.connection

import addHistoryMessageAfterCallback
import core.Player
import core.commands.CommandInterceptor

actual class ConnectionCommandInterceptor : CommandInterceptor {
    actual override fun ignoredCommands(): List<String> {
        return ignoredConnectionCommands
    }

    actual override fun parseCommand(source: Player, line: String) {
        WebClient.sendCommand(line) { responses ->
            responses.forEach {
                addHistoryMessageAfterCallback(it)
            }
        }
    }

}