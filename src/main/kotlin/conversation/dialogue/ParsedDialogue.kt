package conversation.dialogue

import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.target.Target
import core.utility.Named
import core.utility.StringFormatter
import core.utility.capitalize2

data class ParsedDialogue(val speaker: Target, val listener: Target, val subjects: List<Named>, val verb: Verb, val verbOption: String?, val questionType: QuestionType = QuestionType.STATEMENT) {
    val subject = subjects.first()

    fun print(): String {
        val verbOptionSuffix = StringFormatter.format(verbOption != null, " $verbOption", "")
        return "${speaker.name}: ${questionType.name.lowercase().capitalize2()} ${subject.name} ${verb.name.lowercase()}${verbOptionSuffix}?"
    }

    fun matches(questionType: QuestionType? = null, subject: Named? = null, verb: Verb? = null, speaker: Target? = null, listener: Target? = null): Boolean {
        return when {
            questionType != null && questionType != this.questionType -> false
            subject != null && subject != this.subject -> false
            verb != null && verb != this.verb -> false
            speaker != null && speaker != this.speaker -> false
            listener != null && listener != this.listener -> false
            else -> true
        }

    }

}