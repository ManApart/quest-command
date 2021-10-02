package core.commands

import core.target.Target

interface CommandInterceptor {
    fun parseCommand(source: Target, line: String)
}