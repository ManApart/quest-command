package quests

import core.events.EventListener

//TODO - I think we can delete this class
class SetQuestStage : EventListener<SetQuestStageEvent>() {

    override fun execute(event: SetQuestStageEvent) {
        event.quest.executeStage(event.stage, event)
    }


}