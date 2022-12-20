package resources.conversation

import conversation.dialogue.DialogueEvent
import conversation.dsl.*
import conversation.parsing.QuestionType
import conversation.parsing.Verb

class GenericConversations2 {
    val values = conversations2 {
        event { DialogueEvent(it.getLatestListener(), it, "I have nothing to say to you.") }

        child {
            priority = 50
            cond { (it.subjects()?.size ?: 0) > 1 }
            line { "What you mean? You mean ${it.subjects()!![0]} or ${it.subjects()!![1]}?" }
        }

        child {
            question(QuestionType.WHERE)
            verb(Verb.BE)

            child {
                subject { it.getLatestListener() }
                line { "I be here." }
                line { "I be with you." }
            }
            child {
                subject { it.getLatestSpeaker() }
                line { "You be in ${it.getLatestSpeaker().location}." }
            }
        }

        child {
            question(QuestionType.WHAT)
            verb(Verb.BE)
            cond { it.subject().hasTag("City") }
            line { "${it.subject()} be a city." }
        }

        child {
            cond { !it.getLatestListener().isSafe() }
            event { DialogueEvent(it.getLatestListener(), it, "${it.getLatestListener()} is not safe") }
        }

    }
}