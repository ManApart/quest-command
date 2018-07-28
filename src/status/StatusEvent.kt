package status

import core.events.Event
import core.gameState.Creature
import core.gameState.Target
import core.gameState.stat.Stat

class StatusEvent(val creature: Creature) : Event {

    override fun gameTicks(): Int {
        return 0
    }
}