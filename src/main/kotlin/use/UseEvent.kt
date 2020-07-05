package use

import core.events.Event
import core.target.Target

/**
 * A source uses an item on a target. Different from Interact in that there is something being used ON/WITH something else
 */
class UseEvent(val source: Target, val used: Target, val target: Target) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}