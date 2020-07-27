package conversation.statement

import conversation.Verb
import core.events.Event
import core.target.Target
import core.utility.StringFormatter

class StatementEvent(val speaker: Target, val listener: Target, val subject: Target, val verb: Verb, val verbOption: String?) : Event {
    fun print(): String {
        val verbOptionSuffix = StringFormatter.format(verbOption !=null, " $verbOption", "")
        return "${speaker.name}: ${subject.name} ${verb.name.toLowerCase()}${verbOptionSuffix}."
    }
}