package explore.map.compass

import core.events.Event
import core.target.Target

class SetCompassEvent(val source: Target, val locationName: String, val depth: Int) : Event