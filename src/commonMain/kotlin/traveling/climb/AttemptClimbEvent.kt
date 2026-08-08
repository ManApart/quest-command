package traveling.climb

import core.events.Event
import core.thing.Thing
import traveling.direction.Direction
import traveling.location.network.LocationNode

data class AttemptClimbEvent(val creature: Thing, val climbThing: Thing, val desiredDirection: Direction, val quiet: Boolean = false) : Event
//data class AttemptClimbEvent(val creature: Thing, val climbThing: Thing, val desiredDirection: Direction, val goal: LocationNode, val quiet: Boolean = false) : Event
