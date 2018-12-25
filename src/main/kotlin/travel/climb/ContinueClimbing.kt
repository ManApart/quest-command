package travel.climb

import core.events.EventListener
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.climb.ClimbSegment
import core.gameState.stat.Stat
import core.history.display
import core.utility.RandomManager
import core.utility.StringFormatter
import explore.LookEvent
import status.ExpGainedEvent
import status.statChanged.StatChangeEvent
import system.EventManager
import travel.jump.FallEvent

class ContinueClimbing : EventListener<ClimbJourneyEvent>() {
    override fun shouldExecute(event: ClimbJourneyEvent): Boolean {
        return GameState.player.climbJourney is ClimbJourney
    }
    override fun execute(event: ClimbJourneyEvent) {
        val journey = GameState.player.climbJourney as ClimbJourney
        val distance = journey.getDistanceTo(event.desiredStep)
        EventManager.postEvent(StatChangeEvent(GameState.player.creature, "Climbing", Stat.STAMINA, -distance))
        val chance = getChance(journey, event.desiredStep)

        if (RandomManager.isSuccess(chance)) {
            val direction = StringFormatter.format(journey.getDirection(event.desiredStep), "up", "down")
            display("You climb $distance ft $direction ${journey.target.name}.")
            awardEXP(GameState.player.creature, chance, journey.getSegment(event.desiredStep))
            if (journey.isPathEnd(event.desiredStep)) {
                EventManager.postEvent(ClimbCompleteEvent(GameState.player.creature, journey.target, GameState.player.creature.location, journey.getDestination(event.desiredStep)))
            } else {
                journey.advance(event.desiredStep)
                if (event.force){
                    val step = journey.getNextSegments().first().id
                    EventManager.postEvent(ClimbJourneyEvent(step, true))
                } else {
                    EventManager.postEvent(LookEvent())
                }
            }
        } else {
            EventManager.postEvent(FallEvent(GameState.player.creature, journey.bottom, journey.getCurrentDistance(), "You lose your grip on ${journey.target.name}."))
        }

        journey.target.consume(event)
    }


    private fun getChance(journey: ClimbJourney, desiredStep: Int): Double {
        //TODO - more detailed skill check
        val skill = GameState.player.creature.soul.getCurrent(Stat.CLIMBING)
        val challenge = journey.getSegment(desiredStep).level
        return skill / challenge.toDouble()
    }

    private fun awardEXP(creature: Creature, chance: Double, segment: ClimbSegment){
        val amount = if (chance >= 1){
            0
        } else {
            segment.level * segment.distance
        }
        if (amount > 0){
            EventManager.postEvent(ExpGainedEvent(creature, Stat.CLIMBING, amount))
        }
    }

}