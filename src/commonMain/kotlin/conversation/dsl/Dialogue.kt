package conversation.dsl

import conversation.Conversation
import conversation.dialogue.DialogueEvent
import core.events.Event
import core.thing.Thing

private val sampleConvo by lazy { buildSampleConvo() }

class Dialogue(
    val result: suspend (Conversation) -> List<Event>,
    val priority: Int = 10
) {
    private suspend fun getSample(): String {
        return result(sampleConvo).first().toString()
    }
}

private fun buildSampleConvo(): Conversation {
    val speaker = Thing("Speaker")
    val sampleConvo = Conversation(speaker, Thing("Listener"))
    sampleConvo.history.add(DialogueEvent(speaker, sampleConvo, "Sample Line"))
    return sampleConvo
}