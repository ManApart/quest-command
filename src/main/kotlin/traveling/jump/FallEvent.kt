package traveling.jump

import core.events.Event
import core.GameState
import core.target.Target
import traveling.location.location.LocationNode

class FallEvent(val creature: Target = GameState.player, val destination: LocationNode, val fallHeight: Int = 0, val reason: String? = null) : Event