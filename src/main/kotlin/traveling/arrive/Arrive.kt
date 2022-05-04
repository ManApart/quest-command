package traveling.arrive

import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.utility.asSubject
import core.utility.withS

class Arrive : EventListener<ArriveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: ArriveEvent) {
        with(event) {
            if (origin != destination) {
                val player = creature
                player.position = destination.vector
                if (!destination.thingName.isNullOrBlank() && !destination.partName.isNullOrBlank()) {
                    val climbThing = destination.location.getLocation().getThings(destination.thingName).first()
                    val part = climbThing.body.getPartLocation(destination.partName)
                    player.location = part
                    player.setClimbing(climbThing)
                    if (!silent) {
                        creature.display { "${creature.asSubject(it)} ${creature.withS(method, it)} to ${destination}." }
                    }
                } else {
                    player.location = destination.location
                    if (!silent) {
                        if (quiet) {
                            creature.display { "${creature.asSubject(it)} ${creature.withS(method, it)} to ${destination}." }
                        } else {
                            creature.display { "${creature.asSubject(it)} ${creature.withS(method, it)} to ${destination}." }
                            creature.displayToMe("It ${destination.location.getSiblings(false)}.")
                        }
                    }
                }
            }
        }
    }

}