package travel

import core.events.Event
import core.gameState.GameState
import core.gameState.location.LocationNode

class FindRouteEvent(val source: LocationNode = GameState.player.creature.location, val destination: LocationNode, val depth: Int = 5, val startImmediately: Boolean = false) : Event