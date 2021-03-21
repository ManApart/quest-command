package conversation.dialogue

import core.events.EventListener


class DialogueListener : EventListener<DialogueEvent>() {
    override fun execute(event: DialogueEvent) {
        val conversation = event.conversation
        conversation.history.add(event)
        conversation.getLatestListener().ai.hear(event)
    }
}