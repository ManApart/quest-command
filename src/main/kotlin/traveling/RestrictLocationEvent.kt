package traveling

import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class RestrictLocationEvent(val triggeringTarget: Target, val source: LocationNode, val destination: LocationNode, val makeRestricted: Boolean = true, val silent: Boolean = false) : Event