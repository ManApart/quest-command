package traveling

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode

class RestrictLocationEvent(val triggeringThing: Thing, val source: LocationNode, val destination: LocationNode, val makeRestricted: Boolean = true, val silent: Boolean = false) : Event