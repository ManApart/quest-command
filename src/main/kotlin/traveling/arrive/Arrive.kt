package traveling.arrive

import core.events.EventListener
import core.history.display
import core.utility.asSubject

class Arrive : EventListener<ArriveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: ArriveEvent) {
        if (event.origin != event.destination) {
            val player = event.creature
            player.position = event.destination.vector
            if (!event.destination.thingName.isNullOrBlank() && !event.destination.partName.isNullOrBlank()) {
                val climbThing = event.destination.location.getLocation().getThings(event.destination.thingName).first()
                val part = climbThing.body.getPartLocation(event.destination.partName)
                player.location = part
                player.setClimbing(climbThing)
                if (!event.silent) {
                    event.creature.display{"${event.creature.asSubject(it)} ${event.method} to ${event.destination}."}
                }
            } else {
                player.location = event.destination.location
                if (!event.silent) {
                    if (event.quiet) {
                        event.creature.display{"${event.creature.asSubject(it)} ${event.method} to ${event.destination}."}
                    } else {
                        event.creature.display{"${event.creature.asSubject(it)} ${event.method} to ${event.destination}. It ${event.destination.location.getSiblings(false)}."}
                    }
                }
            }
        }
    }

}