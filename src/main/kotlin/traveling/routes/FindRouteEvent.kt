package traveling.routes

import core.GameState
import core.events.Event
import traveling.location.location.LocationNode

class FindRouteEvent(val source: LocationNode = GameState.player.location, val destination: LocationNode, val depth: Int = 5, val startImmediately: Boolean = false, val quiet: Boolean = false) : Event