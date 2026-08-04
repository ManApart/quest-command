package traveling.climb

import core.events.EventListener
import core.events.EventManager
import core.history.display
import core.thing.Thing
import core.utility.RandomManager
import core.utility.asSubject
import core.utility.isAre
import core.utility.withS
import explore.listen.addSoundEffect
import status.ExpGainedEvent
import status.stat.AttributeStrings.STAMINA
import status.stat.SkillStrings.CLIMBING
import status.statChanged.StatChangeEvent
import traveling.arrive.ArriveEvent
import traveling.direction.Direction
import traveling.jump.FallEvent
import traveling.location.location.LocationPoint
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import kotlin.math.max

class AttemptClimb : EventListener<AttemptClimbEvent>() {
    override suspend fun shouldExecute(event: AttemptClimbEvent): Boolean {
        return event.creature.isPlayer() && event.climbThing.properties.tags.has("Climbable")
    }

    override suspend fun complete(event: AttemptClimbEvent) {
        if (!isWithinRange(event)) {
            event.creature.display { event.creature.asSubject(it) + " " + event.creature.isAre(it) + " too far away to climb ${event.climbThing.name}." }
        } else {
            val distance = event.climbThing.body.getHeight()
            val chance = getChance(event.creature, distance)

            event.creature.addSoundEffect("Climbing", "the rough scuffle of two surfaces scraping over each other")
            EventManager.postEvent(StatChangeEvent(event.creature, "Climbing", STAMINA, -distance, event.quiet))
            if (event.creature.getEncumbrance() < 1f && RandomManager.isSuccess(chance)) {
                advance(event, distance, chance)
            } else {
                fall(event, distance)
            }
            event.climbThing.consume(event)
        }
    }

    private suspend fun isWithinRange(event: AttemptClimbEvent): Boolean {
        return event.creature.climbThing != null || event.climbThing.isWithinRangeOf(event.creature)
                || event.climbThing.location != event.creature.location
    }

    private fun getChance(creature: Thing, distance: Int): Double {
        //TODO - segment difficulty by material
        val skill = creature.soul.getCurrent(CLIMBING)
        val segmentDifficulty = 1
        val challenge = max(distance * segmentDifficulty, 1)
        return skill / challenge.toDouble()
    }

    private suspend fun advance(event: AttemptClimbEvent, distance: Int, chance: Double) {
        val directionString = getDirectionString(event.desiredDirection)
        when {
            distance == 0 && event.desiredDirection == Direction.BELOW -> event.creature.display("You descend ${event.climbThing.name}.")
            distance == 0 -> event.creature.display{"${event.creature.asSubject(it)} ${event.creature.withS("climb", it)} ${event.climbThing.name}."}
            else -> event.creature.display{"${event.creature.asSubject(it)} ${event.creature.withS("climb", it)} $distance ft$directionString ${event.climbThing.name}."}
        }

        event.creature.setClimbing(event.climbThing)
        awardEXP(event.creature, chance)

        val connectedLocation = getConnectedLocation(event.climbThing.location, event.climbThing, event.desiredDirection)
        when {
            connectedLocation == null && event.desiredDirection == Direction.ABOVE -> climbToTop(event)
            else -> dismount(event, connectedLocation)
        }
    }

    private fun awardEXP(creature: Thing, chance: Double) {
        val amount = if (chance >= 1) {
            0
        } else {
            ((1 - chance) * 100).toInt()
        }
        if (amount > 0) {
            EventManager.postEvent(ExpGainedEvent(creature, CLIMBING, amount))
        }
    }

    private fun getDirectionString(direction: Direction): String {
        return if (direction == Direction.NONE) {
            ""
        } else {
            " " + direction.name
        }
    }

    private fun getConnectedLocation(thingLocation: LocationNode, climbThing: Thing, direction: Direction): LocationPoint? {
        val neighbors = thingLocation.getNeighborConnections().filter { it.kind == traveling.location.CLIMBING }
        return if (direction == Direction.ABOVE){
            neighbors.firstOrNull { it.source.equals(thingLocation, climbThing) }?.destination
        } else {
            neighbors.firstOrNull { it.destination.equals(thingLocation, climbThing) }?.source
        }
    }

    private fun fall(event: AttemptClimbEvent, distance: Int) {
        EventManager.postEvent(FallEvent(event.creature, event.climbThing.location, distance, "You lose your grip on ${event.climbThing}."))
    }

    private suspend fun climbToTop(event: AttemptClimbEvent) {
        event.creature.position = event.climbThing.position.plusZ(event.climbThing.body.getHeight())
        event.creature.display { event.creature.asSubject(it) + " " + event.creature.withS("climb", it) + " to the top of ${event.climbThing.name}." }
    }

    private fun dismount(event: AttemptClimbEvent, connectedLocation: LocationPoint?) {
        val origin = LocationPoint(event.climbThing.location, event.creature.position, event.climbThing.name)
        val destination = connectedLocation ?: LocationPoint(event.climbThing.location)

        EventManager.postEvent(ClimbCompleteEvent(event.creature, event.climbThing, origin, destination))
    }

}
