package traveling.jump

import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class FallEvent(val creature: Target, val destination: LocationNode, val fallHeight: Int = 0, val reason: String? = null) : Event