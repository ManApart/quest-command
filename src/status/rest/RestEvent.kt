package status.rest

import core.events.Event
import core.gameState.Creature

class RestEvent(val creature: Creature, val hoursRested: Int) : Event {
    override fun gameTicks(): Int {
        return hoursRested
    }
}