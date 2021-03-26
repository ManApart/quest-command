package resources.conversation

import conversation.dialogue.DialogueEvent
import conversation.input.DialogueResource
import conversation.input.conversations
import conversation.parsing.QuestionType
import conversation.parsing.Verb
import traveling.location.location.LocationManager

class GenericConversations : DialogueResource {
    override val values = conversations {
        result { DialogueEvent(it.getLatestListener(), it, "I have nothing to say to you.") }

        convo {
            condition = {  it.history.last().parsed?.matches(QuestionType.WHERE, it.getLatestListener(), Verb.BE) ?: false }
            resultLine { "I be here." }
        }

        convo {
            condition = {  it.history.last().parsed?.matches(QuestionType.WHAT, LocationManager.findLocationInAnyNetwork("Kanbara City"), Verb.BE) ?: false }
            resultLine { "Kanbara be a city." }
        }

        convo {
            condition = { !it.getLatestListener().isSafe() }
            result { DialogueEvent(it.getLatestListener(), it, "${it.getLatestListener()} is not safe") }
        }

    }.build()
}