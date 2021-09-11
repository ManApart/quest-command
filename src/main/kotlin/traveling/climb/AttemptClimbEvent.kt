package traveling.climb

import core.GameState
import core.events.Event
import core.target.Target
import traveling.direction.Direction
import traveling.location.location.LocationNode

class AttemptClimbEvent(val creature: Target = GameState.player, val target: Target, val targetPart: LocationNode, val desiredDirection: Direction, val quiet: Boolean = false) : Event