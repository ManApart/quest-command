package system.gameTick

import core.events.Event

class GameTickEvent(val time: Int = 1) : Event {
    override fun gameTicks(): Int {
        return 0
    }
}