package explore.map

import core.events.Event
import traveling.location.LocationNode

class ReadMapEvent(val target: LocationNode, val depth: Int = 1) : Event