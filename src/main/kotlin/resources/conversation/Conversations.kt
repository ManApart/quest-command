package resources.conversation

import conversation.dialogue.DialogueEvent
import conversation.input.DialogueResource
import conversation.input.convo
import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.GameState

class GenericConversations : DialogueResource {
    override val values = convo({ GameState.player.isPlayer() }) {
        result = { DialogueEvent(it.getLatestListener(), it, "top level comment") }

        convo({ it.history.last().parsed?.matches(QuestionType.WHERE, it.getLatestListener(), Verb.BE) ?: false }) {
            resultLine = { "I be here." }
        }

        convo({ it.getLatestListener().isSafe() }) {
            result = { DialogueEvent(it.getLatestListener(), it, "${it.getLatestListener()} is safe") }
        }

        convo({ !it.getLatestListener().isSafe() }) {
            result = { DialogueEvent(it.getLatestListener(), it, "${it.getLatestListener()} is not safe") }
        }

    }.build()
}