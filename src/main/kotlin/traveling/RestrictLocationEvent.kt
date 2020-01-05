package traveling

import core.events.Event
import traveling.location.location.LocationNode

class RestrictLocationEvent(val source: LocationNode, val destination: LocationNode, val makeRestricted: Boolean = true, val silent: Boolean = false) : Event