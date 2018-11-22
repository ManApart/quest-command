package explore

import core.events.Event
import core.gameState.location.LocationNode

class RestrictLocationEvent(val source: LocationNode, val destination: LocationNode, val makeRestricted: Boolean = true, val silent: Boolean = false) : Event