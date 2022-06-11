package explore.map.compass

import core.Player
import core.events.Event

class ViewCompassEvent(val source: Player, val depth: Int = 100) : Event