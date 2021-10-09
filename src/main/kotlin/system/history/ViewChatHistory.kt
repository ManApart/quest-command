package system.history

import core.events.EventListener
import core.history.ChatHistoryManager
import core.history.InputOutput
import core.history.displayToMe

class ViewChatHistory : EventListener<ViewChatHistoryEvent>() {


    override fun execute(event: ViewChatHistoryEvent) {
        val historyList = ChatHistoryManager.getHistory(event.source).history.takeLast(event.numberOfLinesToShow)
        val message = createMessage(historyList, event.viewResponses)
        event.source.displayToMe(message)
    }

    private fun createMessage(historyList: List<InputOutput>, viewResponses: Boolean): String {
        var message = ""
        historyList.forEach { inputOutput ->
            message += "\n ${inputOutput.input}"
            if (viewResponses) {
                message += ":"
                inputOutput.outPut.forEach {
                    message += "\n\t$it"
                }
            }
        }

        return message.substring(1)
    }


}