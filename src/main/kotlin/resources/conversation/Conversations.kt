package resources.conversation

import conversation.dialogue.DialogueEvent
import conversation.input.*
import conversation.parsing.QuestionType
import conversation.parsing.Verb
import traveling.location.location.LocationManager
import traveling.location.location.LocationNode

class GenericConversations : DialogueResource {
    override val values = conversations {
        result { DialogueEvent(it.getLatestListener(), it, "I have nothing to say to you.") }

        cond({ it.question() == QuestionType.WHERE }) {
            cond({ it.verb() == Verb.BE }) {
                cond({ it.subject() == it.getLatestListener()}) {
                    resultLine { "I be here." }
                }
                cond({ it.subject() == it.getLatestSpeaker()}) {
                    resultLine { "You be in ${it.getLatestSpeaker().location}." }
                }
            }
        }

        cond({ it.question() == QuestionType.WHAT }) {
            cond({ it.verb() == Verb.BE }) {
                cond({ it.subject().hasTag("City") }) {
                    resultLine { "${it.subject()} be a city." }
                }
            }
        }

        cond({ !it.getLatestListener().isSafe() }) {
            result { DialogueEvent(it.getLatestListener(), it, "${it.getLatestListener()} is not safe") }
        }

    }.build()
}