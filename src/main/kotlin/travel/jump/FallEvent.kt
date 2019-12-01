package travel.jump

import core.events.Event
import core.gameState.GameState
import core.gameState.Target
import core.gameState.location.LocationNode

class FallEvent(val creature: Target = GameState.player, val destination: LocationNode, val fallHeight: Int = 0, val reason: String? = null) : Event