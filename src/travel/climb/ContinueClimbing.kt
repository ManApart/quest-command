package travel.climb

import core.commands.CommandParser
import core.events.EventListener
import core.gameState.GameState
import core.gameState.Stat
import core.gameState.consume
import core.utility.RandomManager
import core.utility.StringFormatter
import explore.LookEvent
import status.StatChangeEvent
import system.EventManager
import travel.jump.FallEvent

class ContinueClimbing : EventListener<ClimbJourneyEvent>() {
    override fun shouldExecute(event: ClimbJourneyEvent): Boolean {
        return GameState.journey is ClimbJourney
    }
    override fun execute(event: ClimbJourneyEvent) {
        val journey = GameState.journey as ClimbJourney
        val distance = journey.getDistanceTo(event.desiredStep)
        EventManager.postEvent(StatChangeEvent(GameState.player.creature, "Climbing", Stat.STAMINA, -distance))

        if (isSuccessful(journey, event.desiredStep)) {
            val direction = StringFormatter.format(journey.getDirection(event.desiredStep), "up", "down")
            println("You climb $direction $distance ft.")
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


    private fun isSuccessful(journey: ClimbJourney, desiredStep: Int): Boolean {
        //TODO - more detailed skill check
        val skill = GameState.player.creature.soul.getCurrent(Stat.CLIMBING)
        val challenge = journey.getSegment(desiredStep).level
        val chance = skill / challenge.toDouble()
        return true
//        return RandomManager.isSuccess(chance)
    }



}