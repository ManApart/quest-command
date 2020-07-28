package conversation.dialogue

import core.events.EventListener
import core.history.display


class DialogueListener : EventListener<DialogueEvent>() {
    private val conversationDialogue: DialogueOptions by lazy { DialogueOptionsManager.getConversationResponses() }

    override fun execute(event: DialogueEvent) {
        display(event.print())
        displayResponseIfItExists(event)
    }

    private fun displayResponseIfItExists(event: DialogueEvent) {
        val params = buildParameters(event)
        val response = conversationDialogue.apply(params).getOption()
        if (response != null) {
            display("${event.listener}: $response")
        }
    }

    private fun buildParameters(event: DialogueEvent): Map<String, String> {
        val params = mutableMapOf(
                "speaker" to event.speaker.name.toLowerCase(),
                "listener" to event.listener.name.toLowerCase(),
                "verb" to event.verb.name.toLowerCase(),
                "questionType" to event.questionType.name.toLowerCase(),
                "subject" to event.subject.name.toLowerCase()
        )

        if (event.verbOption != null) {
            params["verbOption"] = event.verbOption.toLowerCase()
        }

        return params
    }
}