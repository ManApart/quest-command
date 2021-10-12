package traveling.jump

import core.GameState
import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class JumpEvent(val creature: Target, val source: LocationNode = GameState.player.target.location, val destination: LocationNode, val fallDistance: Int? = null) : Event
