package use.interaction

import core.events.Event
import core.thing.Thing

/**
 * A thing is interacting with another thing. Different from UseEvent in that nothing is being used ON/WITH the thing.
 */
class InteractEvent(val source: Thing, val thing: Thing) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}