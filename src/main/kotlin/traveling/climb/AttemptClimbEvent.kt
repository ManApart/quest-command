package traveling.climb

import core.events.Event
import core.target.Target
import traveling.direction.Direction
import traveling.location.network.LocationNode

class AttemptClimbEvent(val creature: Target, val target: Target, val targetPart: LocationNode, val desiredDirection: Direction, val quiet: Boolean = false) : Event