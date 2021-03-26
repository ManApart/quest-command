package conversation.input

import conversation.Conversation
import core.events.Event
import core.target.Target

//TODO - make nice toString method
class Dialogue(
    val result: (Conversation) -> List<Event>,
    val conditions: List<(Conversation) -> Boolean>,
    val priority: Int = 10
) {

    override fun toString(): String {
        val sample = result(Conversation(Target("Speaker"), Target("Listener"))).first()
        return "Priority: $priority, Sample: $sample"
    }

    fun matches(conversation: Conversation): Boolean {
        return conditions.all { it(conversation) }
    }
}