package conversation.question

import core.events.EventListener
import core.history.display


class QuestionListener : EventListener<QuestionEvent>() {
    override fun execute(event: QuestionEvent) {
        display(event.print())
    }
}