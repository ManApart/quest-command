package core.events.eventParsers

import core.events.Event
import core.target.Target
import quests.triggerCondition.TriggeredEvent
import quests.QuestManager
import quests.SetQuestStageEvent

class SetQuestStageEventParser : EventParser {
    override fun className(): String {
        return SetQuestStageEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val questP = 0
        val stageP = 1

        val quest = QuestManager.quests.get(event.getParam(questP))
        val stage = event.getParamInt(stageP)

        return SetQuestStageEvent(quest, stage)
    }
}