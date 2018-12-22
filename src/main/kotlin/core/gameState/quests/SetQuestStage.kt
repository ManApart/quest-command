package core.gameState.quests

import core.events.EventListener

class SetQuestStage : EventListener<SetQuestStageEvent>() {

    override fun execute(event: SetQuestStageEvent) {
        event.quest.executeStage(event.stage)
    }


}