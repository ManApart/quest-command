package conversation.dsl

import conversation.Conversation
import conversation.dialogue.DialogueEvent
import core.events.Event
import core.thing.Thing

private val sampleConvo by lazy { buildSampleConvo() }

class Dialogue(
    val conditions: List<(Conversation) -> Boolean>,
    val result: (Conversation) -> List<Event>,
    val priority: Int = 10
) {

    override fun toString(): String {
        val sample = result(sampleConvo).first()
        return "Priority: $priority, Sample: $sample"
    }

    fun matches(conversation: Conversation): Boolean {
        return conditions.all { it(conversation) }
    }
}

private fun buildSampleConvo(): Conversation {
    val speaker = Thing("Speaker")
    val sampleConvo = Conversation(speaker, Thing("Listener"))
    sampleConvo.history.add(DialogueEvent(speaker, sampleConvo, "Sample Line"))
    return sampleConvo
}