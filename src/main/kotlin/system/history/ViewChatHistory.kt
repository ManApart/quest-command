package system.history

import core.events.EventListener
import core.history.ChatHistory
import core.history.InputOutput
import core.history.display

class ViewChatHistory : EventListener<ViewChatHistoryEvent>() {


    override fun execute(event: ViewChatHistoryEvent) {
        val historyList = ChatHistory.history.takeLast(event.numberOfLinesToShow)
        val message = createMessage(historyList, event.viewResponses)
        display(message)
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