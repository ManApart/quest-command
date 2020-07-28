package conversation.dialogue

import core.events.EventListener
import core.history.display


class DialogueListener : EventListener<DialogueEvent>() {
    private val conversationDialogue: List<ConditionalDialogue> by lazy { DialogueOptionsManager.getConversationResponses() }

    override fun execute(event: DialogueEvent) {
        display(event.print())
        displayResponseIfItExists(event)
    }

    private fun displayResponseIfItExists(event: DialogueEvent) {
        conversationDialogue.firstOrNull {
            it.matches(event)
        }?.execute(event.listener, event.getFieldsAsParams())
    }

}