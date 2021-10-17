package explore.look

import core.GameState
import core.events.EventListener
import core.properties.IS_CLIMBING

class Look : EventListener<LookEvent>() {

    override fun execute(event: LookEvent) {
        when {
            event.source.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney(GameState.getPlayer(event.source))
            event.thing != null -> describeThing(event.thing)
            event.source.ai.aggroThing != null -> describeBattle(event.source)
            else -> describeLocation(event.source)
        }
    }

}