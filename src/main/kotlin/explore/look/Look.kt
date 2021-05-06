package explore.look

import core.GameState
import core.events.EventListener
import core.properties.IS_CLIMBING

class Look : EventListener<LookEvent>() {

    override fun execute(event: LookEvent) {
        when {
            GameState.player.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney()
            event.target != null -> describeTarget(event.target)
            event.source.ai.aggroTarget != null -> describeBattle()
            else -> describeLocation()
        }
    }

}