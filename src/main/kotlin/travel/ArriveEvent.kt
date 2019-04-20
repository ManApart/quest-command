package travel

import core.events.Event
import core.gameState.Target
import core.gameState.GameState
import core.gameState.location.LocationNode

class ArriveEvent(val creature: Target = GameState.player, val origin: LocationNode = GameState.player.location, val destination: LocationNode, val method: String) : Event
