package explore.map

import core.Player
import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class ReadMapEvent(val source: Player, val target: LocationNode, val depth: Int = 1) : Event