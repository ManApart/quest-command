package traveling.arrive

import core.GameState.player
import core.events.EventListener
import core.history.display
import core.history.displayToMe
import core.history.displayToOthers
import core.utility.asSubject
import core.utility.withS

class Arrive : EventListener<ArriveEvent>() {

    override fun getPriorityRank() = 10

    override suspend fun complete(event: ArriveEvent) {
        with(event) {
            if (origin != destination) {
                creature.setNotClimbing()
                creature.location = destination.location
                if (!destination.thingName.isNullOrBlank()) {
                    val destThing = destination.location.getLocation().getThings(destination.thingName).first()
                    creature.position = destThing.position
                    if (!silent) creature.display { "${creature.asSubject(it)} ${creature.withS(method, it)} to ${destination}." }
                } else {
                    creature.position = destination.vector
                    if (!silent) {
                        if (quiet) {
                            creature.display { "${creature.asSubject(it)} ${creature.withS(method, it)} to ${destination}." }
                        } else {
                            creature.display { "${creature.asSubject(it)} ${creature.withS(method, it)} to ${destination}." }
                            creature.displayToMe("It ${destination.location.getSiblings(false)}.")
                        }
                    }
                }
                creature.mind.route?.let { route ->
                    if (route.destination == player.location) {
                        creature.mind.route = null
                    }
                }
            }
        }
    }

}
