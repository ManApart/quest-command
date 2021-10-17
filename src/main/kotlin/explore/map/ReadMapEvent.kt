package explore.map

import core.Player
import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode

class ReadMapEvent(val source: Player, val thing: LocationNode, val depth: Int = 1) : Event