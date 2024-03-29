package traveling.arrive

import core.events.Event
import core.thing.Thing
import traveling.location.location.LocationPoint

data class ArriveEvent(val creature: Thing, val origin: LocationPoint = LocationPoint(creature.location), val destination: LocationPoint, val method: String, val quiet: Boolean = false, val silent: Boolean = false) : Event
