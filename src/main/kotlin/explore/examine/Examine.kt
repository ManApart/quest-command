package explore.examine

import core.GameState
import core.events.EventListener
import core.properties.IS_CLIMBING
import explore.look.describeBattle
import explore.look.describeClimbJourney
import explore.look.describeLocationDetailed
import explore.look.describeTarget

class Examine : EventListener<ExamineEvent>() {

    override fun execute(event: ExamineEvent) {
        when {
            GameState.player.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney()
            event.target != null -> describeTarget(event.target)
            event.source.ai.aggroTarget != null -> describeBattle()
            else -> describeLocationDetailed()
        }
    }



}