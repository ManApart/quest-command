package conversation

import conversation.dialogue.DialogueEvent
import core.thing.Thing

class Conversation(private val firstSpeaker: Thing, private val firstListener: Thing) {
    val history: MutableList<DialogueEvent> = mutableListOf()

    fun getLatestSpeaker(): Thing{
        return history.lastOrNull()?.speaker ?: firstSpeaker
    }

    fun getLatestListener() : Thing {
        return getListener(getLatestSpeaker())
    }

    private fun getListener(speaker: Thing): Thing {
        return if (firstSpeaker == speaker) {
            firstListener
        } else {
            firstSpeaker
        }
    }

}
