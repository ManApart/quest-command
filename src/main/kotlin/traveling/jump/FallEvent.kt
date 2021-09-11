package traveling.jump

import core.GameState
import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class FallEvent(val creature: Target = GameState.player, val destination: LocationNode, val fallHeight: Int = 0, val reason: String? = null) : Event