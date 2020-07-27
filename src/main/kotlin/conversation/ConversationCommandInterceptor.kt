package conversation

import core.commands.CommandInterceptor
import core.target.Target

class ConversationCommandInterceptor(private val speaker: Target, private val listener: Target) : CommandInterceptor {
    override fun parseCommand(command: String) {
        println("Said: $command")
    }
}