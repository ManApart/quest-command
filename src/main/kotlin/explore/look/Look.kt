package explore.look

import core.GameState
import core.events.EventListener
import core.history.display
import core.properties.IS_CLIMBING
import core.target.Target
import core.target.targetsToString
import traveling.position.NO_VECTOR

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