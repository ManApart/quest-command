package conversation.dialogue

import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.target.Target
import core.utility.Named
import core.utility.StringFormatter
import traveling.location.location.LocationNode

data class ParsedDialogue(val speaker: Target, val listener: Target, val subject: Named, val verb: Verb, val verbOption: String?, val questionType: QuestionType = QuestionType.STATEMENT) {

    fun print(): String {
        val verbOptionSuffix = StringFormatter.format(verbOption != null, " $verbOption", "")
        return "${speaker.name}: ${questionType.name.toLowerCase().capitalize()} ${subject.name} ${verb.name.toLowerCase()}${verbOptionSuffix}?"
    }

    fun getFieldsAsParams(): Map<String, String> {
        val params = mutableMapOf(
                "speaker" to speaker.name.toLowerCase(),
                "listener" to listener.name.toLowerCase(),
                "subject" to subject.name.toLowerCase(),
                "verb" to verb.name.toLowerCase(),
                "questionType" to questionType.name.toLowerCase()
        )
        if (verbOption != null) {
            params["verbOption"] = verbOption.toLowerCase()
        }

        params["subjectType"] = when (subject) {
            is Target -> "target"
            is LocationNode -> "location"
            else -> "named"
        }

        return params
    }

}