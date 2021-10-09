package explore.map.compass

import core.events.Event
import core.target.Target

class ViewCompassEvent(val source: Target, val depth: Int = 100) : Event