package travel.climb

import core.events.EventListener
import core.gameState.Direction
import core.gameState.GameState
import core.gameState.Target
import core.gameState.location.LocationNode
import core.gameState.location.RouteFinder
import core.gameState.stat.CLIMBING
import core.gameState.stat.STAMINA
import core.history.display
import core.utility.RandomManager
import explore.LookEvent
import status.ExpGainedEvent
import status.statChanged.StatChangeEvent
import system.EventManager
import travel.ArriveEvent
import travel.jump.FallEvent

class AttemptClimb : EventListener<AttemptClimbEvent>() {
    override fun shouldExecute(event: AttemptClimbEvent): Boolean {
        return event.creature == GameState.player && event.target.properties.tags.has("Climbable")
    }

    override fun execute(event: AttemptClimbEvent) {
        val distance = getDistance(event.creature.location, event.targetPart)
        val chance = getChance(event.creature, distance)

        EventManager.postEvent(StatChangeEvent(GameState.player, "Climbing", STAMINA, -distance))
        if (RandomManager.isSuccess(chance)) {
            advance(event, distance, chance)
        } else {
            fall(event)
        }
        event.target.consume(event)
    }

    private fun getDistance(source: LocationNode, destination: LocationNode): Int {
        return if (source.parent == destination.parent) {
            val routeFinder = RouteFinder(source, destination)
            if (routeFinder.hasRoute()) {
                routeFinder.getRoute().getDistance()
            } else {
                0
            }
        } else {
            destination.getDistanceToLowestNodeInNetwork()
        }
    }

    private fun advance(event: AttemptClimbEvent, distance: Int, chance: Double) {
        val direction = getDirectionString(event.creature.location, event.targetPart)
        display("You climb $distance ft$direction towards ${event.targetPart.name}.")
        GameState.player.setClimbing(event.target)
        awardEXP(GameState.player, chance)
        EventManager.postEvent(ArriveEvent(event.creature, event.creature.location, event.targetPart, "Climb", true))
        EventManager.postEvent(LookEvent())
    }

    private fun getDirectionString(source: LocationNode, destination: LocationNode): String {
        if (source.parent == destination.parent) {
            val routeFinder = RouteFinder(source, destination)
            if (routeFinder.hasRoute()) {
                return " " + routeFinder.getRoute().getConnections().first().vector.direction.name
            }
        }
        return ""
    }


    private fun getChance(creature: Target, segmentDistance: Int): Double {
        //TODO - segment difficulty by material
        val skill = creature.soul.getCurrent(CLIMBING)
        val segmentDifficulty = 1
        val challenge = Math.max(segmentDistance * segmentDifficulty, 1)
        return skill / challenge.toDouble()
    }

    private fun awardEXP(creature: Target, chance: Double) {
        val amount = if (chance >= 1) {
            0
        } else {
            ((1 - chance) * 100).toInt()
        }
        if (amount > 0) {
            EventManager.postEvent(ExpGainedEvent(creature, CLIMBING, amount))
        }
    }


    private fun fall(event: AttemptClimbEvent) {
        EventManager.postEvent(FallEvent(event.creature, event.target.location, event.creature.location.getDistanceToLowestNodeInNetwork(), "You lose your grip on ${event.targetPart.name}."))
    }

}