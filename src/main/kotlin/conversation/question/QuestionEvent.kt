package conversation.question

import conversation.QuestionType
import conversation.Verb
import core.events.Event
import core.target.Target
import core.utility.StringFormatter

class QuestionEvent(val speaker: Target, val listener: Target, val type: QuestionType, val subject: Target, val verb: Verb, val verbOption: String?) : Event {
    fun print(): String {
        val verbOptionSuffix = StringFormatter.format(verbOption !=null, " $verbOption", "")
        return "${speaker.name}: ${type.name.toLowerCase().capitalize()} ${subject.name} ${verb.name.toLowerCase()}${verbOptionSuffix}?"
    }
}