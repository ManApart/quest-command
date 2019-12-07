package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import core.gameState.quests.CompleteQuestEvent
import core.gameState.quests.QuestManager

class CompleteQuestEventParser : EventParser {
    override fun className(): String {
        return CompleteQuestEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val messageName = 0

        return CompleteQuestEvent(QuestManager.quests.get(event.getParam(messageName)))
    }
}