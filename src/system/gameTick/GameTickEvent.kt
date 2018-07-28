package system.gameTick

import core.events.Event

class GameTickEvent(val time: Int) : Event {
    override fun gameTicks(): Int {
        return 0
    }
}