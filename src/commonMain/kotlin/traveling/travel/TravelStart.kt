package traveling.travel

import core.events.EventListener
import core.events.EventManager
import core.history.displayToMe
import core.history.displayToOthers
import core.thing.Thing
import core.utility.clamp
import explore.listen.addSoundEffect
import status.stat.SNEAK
import status.stat.STAMINA
import status.stat.getStaminaCost
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.location.location.LocationPoint
import traveling.move.MoveEvent
import kotlin.math.max

class TravelStart : EventListener<TravelStartEvent>() {
    override suspend fun execute(event: TravelStartEvent) {
        val connection = event.currentLocation.getConnection(event.destination)
        if (connection == null){
            event.creature.displayToMe("Could not find a way to get to ${event.destination.name}.")
            return
        }
        val vector = event.creature.position - connection.source.vector
        val stamina = event.creature.soul.getCurrent(STAMINA)
        val requiredStamina = getStaminaCost(vector)
        when {
            event.destination == event.currentLocation -> event.creature.displayToMe("You realize that you're already at ${event.currentLocation}")
            event.currentLocation.isMovingToRestricted(event.destination) -> event.creature.displayToMe("You're not able to get to ${event.destination.name}")
            stamina == 0 -> event.creature.displayToMe("You're too tired to travel.")
            stamina < requiredStamina ->  EventManager.postEvent(MoveEvent(event.creature, destination = connection.source.vector))
            !event.creature.isSafe() -> event.creature.displayToMe("You can't travel right now.")
            event.creature.getEncumbrance() >= 1 -> event.creature.displayToMe("You are too encumbered to travel.")
            else -> {
                if (!event.quiet) {
                    event.creature.displayToOthers { "${event.creature.name} leaves ${event.currentLocation} heading ${connection.vector.direction.name.lowercase()}."  }
                    event.creature.displayToMe("You leave ${event.currentLocation} travelling towards ${event.destination}.")
                }
                postArriveEvent(event.creature, connection.destination, requiredStamina, event.quiet)
            }
        }
    }
}

suspend fun postArriveEvent(source: Thing, destination: LocationPoint, requiredStamina: Int, quiet: Boolean) {
    val soundLevel = (max(10, (source.getEncumbrance() * 100).toInt()) - source.soul.getCurrent(SNEAK)).clamp(0, 20)
    source.addSoundEffect("Moving", "the sound of footfalls", soundLevel)
    EventManager.postEvent(ArriveEvent(source, destination = destination, method = "travel", quiet = quiet))
    EventManager.postEvent(StatChangeEvent(source, "The journey", STAMINA, -requiredStamina, silent = quiet))
}