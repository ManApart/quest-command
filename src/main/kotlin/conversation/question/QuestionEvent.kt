package conversation.question

import conversation.QuestionType
import conversation.Verb
import core.events.Event
import core.target.Target

class QuestionEvent(val speaker: Target, val listener: Target, val type: QuestionType, val subject: Target, val verb: Verb) : Event {
    fun print(): String {
        return "${speaker.name}: ${type.name.toLowerCase().capitalize()} ${subject.name} ${verb.name.toLowerCase()}?"
    }
}