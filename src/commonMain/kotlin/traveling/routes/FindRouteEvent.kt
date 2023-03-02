package traveling.routes

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode

data class FindRouteEvent(val source: Thing, val sourceLocation: LocationNode, val destination: LocationNode, val depth: Int = 5, val startImmediately: Boolean = false, val quiet: Boolean = false) : Event