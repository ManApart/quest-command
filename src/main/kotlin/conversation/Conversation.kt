package conversation

import conversation.dialogue.DialogueEvent
import core.target.Target

class Conversation(private val firstSpeaker: Target, private val firstListener: Target) {
    val history: MutableList<DialogueEvent> = mutableListOf()

    fun getLatestSpeaker(): Target{
        return history.lastOrNull()?.speaker ?: firstSpeaker
    }

    fun getLatestListener() : Target {
        return getListener(getLatestSpeaker())
    }

    private fun getListener(speaker: Target): Target {
        return if (firstSpeaker == speaker) {
            firstListener
        } else {
            firstSpeaker
        }
    }

}
