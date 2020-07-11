package traveling.arrive

import core.GameState
import core.events.EventListener
import core.history.display
import traveling.position.Vector

class Arrive : EventListener<ArriveEvent>() {

    override fun getPriorityRank(): Int {
        return 10
    }

    override fun execute(event: ArriveEvent) {
        if (event.origin != event.destination) {
            GameState.player.position = Vector()
            if (!event.destination.targetName.isNullOrBlank() && !event.destination.partName.isNullOrBlank()) {
                val climbTarget = event.destination.location.getLocation().getTargets(event.destination.targetName).first()
                val part = climbTarget.body.getPartLocation(event.destination.partName)
                GameState.player.location = part
                GameState.player.setClimbing(climbTarget)
                if (!event.silent) {
                    display("You ${event.method} to ${event.destination}.")
                }
            } else {
                GameState.player.location = event.destination.location
                if (!event.silent) {
                    if (event.quiet) {
                        display("You ${event.method} to ${event.destination}.")
                    } else {
                        display("You ${event.method} to ${event.destination}. It ${event.destination.location.getSiblings()}.")
                    }
                }
            }
        }
    }

}