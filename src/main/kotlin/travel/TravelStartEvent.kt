package travel

import core.events.Event
import core.gameState.Target
import core.gameState.GameState
import core.gameState.location.LocationNode

class TravelStartEvent(val creature: Target = GameState.player, val currentLocation: LocationNode = GameState.player.location, val destination: LocationNode, val quiet: Boolean = false) : Event