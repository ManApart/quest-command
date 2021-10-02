package explore.look

import core.events.EventListener
import core.properties.IS_CLIMBING

class Look : EventListener<LookEvent>() {

    override fun execute(event: LookEvent) {
        when {
            event.source.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney(event.source)
            event.target != null -> describeTarget(event.target)
            event.source.ai.aggroTarget != null -> describeBattle(event.source)
            else -> describeLocation(event.source)
        }
    }

}