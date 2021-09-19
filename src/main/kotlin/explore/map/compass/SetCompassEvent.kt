package explore.map.compass

import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class SetCompassEvent(val source: Target, val locationName: String, val depth: Int) : Event