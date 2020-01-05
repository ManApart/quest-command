package traveling.jump

import core.events.Event
import core.GameState
import core.target.Target
import traveling.location.location.LocationNode

class JumpEvent(val creature: Target = GameState.player, val source: LocationNode = GameState.player.location, val destination: LocationNode, val fallDistance: Int? = null) : Event