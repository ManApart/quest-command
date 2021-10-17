package traveling.arrive

import core.GameState
import core.events.Event
import core.thing.Thing
import traveling.location.location.LocationPoint

class ArriveEvent(val creature: Thing, val origin: LocationPoint = LocationPoint(GameState.player.thing.location), val destination: LocationPoint, val method: String, val quiet: Boolean = false, val silent: Boolean = false) : Event
