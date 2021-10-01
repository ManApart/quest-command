package use.interaction

import core.events.Event
import core.target.Target

/**
 * A target is interacting with another target. Different from UseEvent in that nothing is being used ON/WITH the target.
 */
class InteractEvent(val source: Target, val target: Target) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}