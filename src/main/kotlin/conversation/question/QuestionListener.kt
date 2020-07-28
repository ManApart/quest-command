package conversation.question

import core.events.EventListener
import core.history.display


class QuestionListener : EventListener<DialogueEvent>() {
    override fun execute(event: DialogueEvent) {
        display(event.print())
    }
}