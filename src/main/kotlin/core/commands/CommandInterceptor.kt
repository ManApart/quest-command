package core.commands

import core.Player

interface CommandInterceptor {
    fun parseCommand(source: Player, line: String)
}