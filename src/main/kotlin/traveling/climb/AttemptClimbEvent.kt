package traveling.climb

import core.events.Event
import traveling.direction.Direction
import core.GameState
import core.target.Target
import traveling.location.location.LocationNode

class AttemptClimbEvent(val creature: Target = GameState.player, val target: Target, val targetPart: LocationNode, val desiredDirection: Direction, val quiet: Boolean = false) : Event