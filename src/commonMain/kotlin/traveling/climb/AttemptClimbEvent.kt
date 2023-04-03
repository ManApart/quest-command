package traveling.climb

import core.events.Event
import core.thing.Thing
import traveling.direction.Direction
import traveling.location.network.LocationNode

data class AttemptClimbEvent(val creature: Thing, val thing: Thing, val thingPart: LocationNode, val desiredDirection: Direction, val quiet: Boolean = false) : Event