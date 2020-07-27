package conversation.statement

import conversation.Verb
import core.events.Event
import core.target.Target

class StatementEvent(val speaker: Target, val listener: Target, val subject: Target, val verb: Verb) : Event {
    fun print(): String {
        return "${speaker.name}: ${subject.name} ${verb.name.toLowerCase()}."
    }
}