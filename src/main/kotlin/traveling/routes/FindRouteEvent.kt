package traveling.routes

import core.GameState
import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class FindRouteEvent(val source: Target, val sourceLocation: LocationNode, val destination: LocationNode, val depth: Int = 5, val startImmediately: Boolean = false, val quiet: Boolean = false) : Event