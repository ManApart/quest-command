package system.history

import core.commands.Args
import core.commands.Command
import core.events.EventManager

class HistoryCommand : Command() {

    override fun getAliases(): List<String> {
        return listOf("History")
    }

    override fun execute(keyword: String, args: List<String>) {
        val arguments = Args(args)
        val showResponses = arguments.contains("responses")
        val numberOfLinesToShow = arguments.getNumber() ?: 10

        EventManager.postEvent(ViewChatHistoryEvent(numberOfLinesToShow, showResponses))
    }

    override fun getDescription(): String {
        return "View the chat history."
    }

    override fun getManual(): String {
        return """
	History - View the recent chat history.
	History <number> - View <number> lines of chat history
	History responses - View both commands and responses"""
    }

    override fun getCategory(): List<String> {
        return listOf("Debugging")
    }


}