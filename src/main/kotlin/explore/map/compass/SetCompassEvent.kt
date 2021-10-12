package explore.map.compass

import core.Player
import core.events.Event

class SetCompassEvent(val source: Player, val locationName: String, val depth: Int) : Event