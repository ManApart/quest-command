package core.commands

interface CommandInterceptor {
    fun parseCommand(command: String)
}