package conversation

import conversation.start.StartConversationEvent
import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.display
import core.target.Target

class SpeakCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Speak", "spk", "talk")
    }

    override fun getDescription(): String {
        return "Speak:\n\tStart a conversation with someone."
    }

    override fun getManual(): String {
        return "\n\tSpeak with <target> - Start a conversation with someone." +
                "\n\tTo stop talking, type 'goodbye' or 'exit'." +
                "\n\tTo ask a question, end your statement with '?'." +
                "\n\tEx: 'Where you are?'." +
                "\n\tAvailable question types: " + QuestionType.values().map { it.name.toLowerCase().capitalize() } +
                "\n\tAvailable verbs: " + Verb.values().map { it.name.toLowerCase().capitalize() }
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("to", "with"))
        when {
            arguments.hasGroup("with") -> speakTo(source, arguments.getString("with"))
            arguments.hasGroup("to") -> speakTo(source, arguments.getString("to"))
            else -> display("Couldn't find someone to speak with.")
        }
    }

    private fun speakTo(speaker: Target, targetName: String) {
        val targets = speaker.location.getLocation().getTargets(targetName)
        if (targets.size == 1) {
            EventManager.postEvent(StartConversationEvent(speaker, targets.first()))
        } else {
            val creatures = speaker.location.getLocation().getCreatures(speaker)
            val message = "Speak to who?\n\t${creatures.joinToString(", ")}"
            val response = ResponseRequest(message, creatures.map { it.name to "speak to ${it.name}." }.toMap())
            CommandParser.setResponseRequest(response)
        }
    }
}