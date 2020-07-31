package core.events.eventParsers

import conversation.dialogue.DialogueEvent
import conversation.parsing.QuestionType
import conversation.parsing.Verb
import core.GameState
import core.events.Event
import core.target.Target
import core.utility.apply
import quests.triggerCondition.TriggeredEvent
import system.message.MessageEvent

class DialogueEventParser : EventParser {

    override fun className(): String {
        return DialogueEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target, params: Map<String, String>): Event {
        val speakerP = 0
        val listenerP = 1
        val subjectP = 2
        val verbP = 3
        val questionTypeP = 4
        val verbOptionP = 5

        //TODO - get from any target, not just local
        val speaker = event.getTargetCreature(parent, speakerP)!!
        val listener = event.getTargetCreature(parent, listenerP)!!
        val subject = event.getTargetCreature(parent, subjectP)!!
        val verb = Verb.BE
        val verbOption = null
        val questionType = QuestionType.STATEMENT

        val messageP = 0

        val message = event.getParam(messageP).apply(params).capitalize()

        return DialogueEvent(speaker, listener, subject, verb, verbOption, questionType)
    }
}