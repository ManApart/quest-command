package core.commands

import core.Player

interface CommandInterceptor {
    fun ignoredCommands(): List<String>
    suspend fun parseCommand(source: Player, line: String)
}