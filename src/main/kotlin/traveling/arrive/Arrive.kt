package traveling.arrive

import core.events.EventListener
import core.history.display
import core.utility.asSubject
import traveling.position.Vector

class Arrive : EventListener<ArriveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: ArriveEvent) {
        if (event.origin != event.destination) {
            val player = event.creature
            player.position = Vector()
            if (!event.destination.targetName.isNullOrBlank() && !event.destination.partName.isNullOrBlank()) {
                val climbTarget = event.destination.location.getLocation().getTargets(event.destination.targetName).first()
                val part = climbTarget.body.getPartLocation(event.destination.partName)
                player.location = part
                player.setClimbing(climbTarget)
                if (!event.silent) {
                    event.creature.display("${event.creature.asSubject()} ${event.method} to ${event.destination}.")
                }
            } else {
                player.location = event.destination.location
                if (!event.silent) {
                    if (event.quiet) {
                        event.creature.display("${event.creature.asSubject()} ${event.method} to ${event.destination}.")
                    } else {
                        event.creature.display("${event.creature.asSubject()} ${event.method} to ${event.destination}. It ${event.destination.location.getSiblings(false)}.")
                    }
                }
            }
        }
    }

}