package traveling.climb

import core.events.Event
import core.thing.Thing
import traveling.direction.Direction

data class AttemptClimbEvent(val creature: Thing, val climbThing: Thing, val desiredDirection: Direction, val quiet: Boolean = false) : Event
