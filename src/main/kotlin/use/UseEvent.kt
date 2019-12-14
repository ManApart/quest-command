package use

import core.events.Event
import core.target.Target

class UseEvent(val source: Target, val used: Target, val target: Target) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}