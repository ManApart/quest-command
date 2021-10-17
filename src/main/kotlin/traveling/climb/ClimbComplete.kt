package traveling.climb

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.utility.asSubject
import traveling.arrive.ArriveEvent

class ClimbComplete : EventListener<ClimbCompleteEvent>() {
    override fun shouldExecute(event: ClimbCompleteEvent): Boolean {
        return event.creature.isPlayer()
    }

    override fun execute(event: ClimbCompleteEvent) {
        event.climbThing.consume(event)
        val climbBackOff = event.destination.location == event.origin.location

        if (climbBackOff) {
            event.creature.display{"${event.creature.asSubject(it)} climb back off ${event.climbThing.name}."}
        }

        val position = event.origin.location.getPositionRelativeTo(event.destination.location)
        val method = if (position != null && position.z > 0) "descend" else "climb"

        EventManager.postEvent(ArriveEvent(event.creature, event.origin, event.destination, method, silent = climbBackOff))
        event.creature.finishClimbing()
    }
}