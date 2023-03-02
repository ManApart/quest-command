package use.interaction

import core.events.Event
import core.thing.Thing

/**
 * A thing is interacting with another thing. Different from UseEvent in that nothing is being used ON/WITH the thing.
 */
data class InteractEvent(val source: Thing, val interactionTarget: Thing) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}