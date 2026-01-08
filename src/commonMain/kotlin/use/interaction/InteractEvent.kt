package use.interaction

import core.events.TemporalEvent
import core.thing.Thing

/**
 * A thing is interacting with another thing. Different from UseEvent in that nothing is being used ON/WITH the thing.
 */
data class InteractEvent(override val creature: Thing, val interactionTarget: Thing, override var timeLeft: Int = 1, val ignoreDistance: Boolean = false) : TemporalEvent
