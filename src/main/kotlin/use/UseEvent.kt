package use

import core.events.Event
import core.thing.Thing

/**
 * A source uses an item on a thing. Different from Interact in that there is something being used ON/WITH something else
 */
class UseEvent(val source: Thing, val used: Thing, val thing: Thing) : Event {
    override fun gameTicks(): Int = 1
    override fun isExecutableByAI(): Boolean = true
}