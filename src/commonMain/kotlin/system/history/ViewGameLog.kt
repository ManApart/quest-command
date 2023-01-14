package system.history

import core.events.EventListener
import core.history.GameLogger
import core.history.InputOutput
import core.history.displayToMe

class ViewGameLog : EventListener<ViewGameLogEvent>() {


    override suspend fun execute(event: ViewGameLogEvent) {
        val historyList = GameLogger.getHistory(event.source).history.takeLast(event.numberOfLinesToShow)
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