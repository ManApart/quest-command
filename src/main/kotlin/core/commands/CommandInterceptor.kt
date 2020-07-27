package core.commands

interface CommandInterceptor {
    fun parseCommand(line: String)
}