package resources.conversation

import conversation.dialogue.DialogueEvent
import conversation.dsl.*
import conversation.parsing.QuestionType
import conversation.parsing.Verb

class GenericConversations : DialogueTreeResource {
    override val values = conversations {
        event { DialogueEvent(it.getLatestListener(), it, "I have nothing to say to you.") }

        branch {
            priority = 50
            cond { (it.subjects()?.size ?: 0) > 1 }
            line { "What you mean? You mean ${it.subjects()!![0]} or ${it.subjects()!![1]}?" }
        }

        branch {
            question(QuestionType.WHERE)
            verb(Verb.BE)

            branch {
                subject { it.getLatestListener() }
                line { "I be here." }
                line { "I be with you." }
            }
            branch {
                subject { it.getLatestSpeaker() }
                line { "You be in ${it.getLatestSpeaker().location}." }
            }
        }

        branch {
            question(QuestionType.WHAT)
            verb(Verb.BE)
            cond { it.subject().hasTag("City") }
            line { "${it.subject()} be a city." }
        }

        branch {
            cond { !it.getLatestListener().isSafe() }
            event { DialogueEvent(it.getLatestListener(), it, "${it.getLatestListener()} is not safe") }
        }

    }
}