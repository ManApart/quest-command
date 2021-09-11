package traveling.jump

import core.GameState
import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class JumpEvent(val creature: Target = GameState.player, val source: LocationNode = GameState.player.location, val destination: LocationNode, val fallDistance: Int? = null) : Event