package conversation.input

import conversation.Conversation
import core.events.Event

//TODO - make nice toString method
class Dialogue(val result: (Conversation) -> List<Event>, val conditions: List<(Conversation) -> Boolean>, val priority: Int = 10) {
    fun matches(conversation: Conversation): Boolean {
        return conditions.all { it(conversation) }
    }
}