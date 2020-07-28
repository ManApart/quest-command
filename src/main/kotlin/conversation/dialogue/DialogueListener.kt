package conversation.dialogue

import core.events.EventListener
import core.history.display


class DialogueListener : EventListener<DialogueEvent>() {
    override fun execute(event: DialogueEvent) {
        display(event.print())
    }
}