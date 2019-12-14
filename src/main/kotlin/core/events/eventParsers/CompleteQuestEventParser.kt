package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import quests.CompleteQuestEvent
import quests.QuestManager

class CompleteQuestEventParser : EventParser {
    override fun className(): String {
        return CompleteQuestEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val messageName = 0

        return CompleteQuestEvent(QuestManager.quests.get(event.getParam(messageName)))
    }
}