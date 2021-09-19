package explore.map.compass

import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class ViewCompassEvent(val source: Target, val depth: Int = 100) : Event