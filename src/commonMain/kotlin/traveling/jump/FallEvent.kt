package traveling.jump

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode

class FallEvent(val creature: Thing, val destination: LocationNode, val fallHeight: Int = 0, val reason: String? = null) : Event