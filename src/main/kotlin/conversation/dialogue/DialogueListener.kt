package conversation.dialogue

import core.events.EventListener


class DialogueListener : EventListener<DialogueEvent>() {
    override fun execute(event: DialogueEvent) {
        event.listener.ai.hear(event)
    }
}