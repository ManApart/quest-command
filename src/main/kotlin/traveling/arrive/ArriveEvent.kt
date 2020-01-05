package traveling.arrive

import core.GameState
import core.events.Event
import core.target.Target
import traveling.location.location.LocationPoint

class ArriveEvent(val creature: Target = GameState.player, val origin: LocationPoint = LocationPoint(GameState.player.location), val destination: LocationPoint, val method: String, val quiet: Boolean = false, val silent: Boolean = false) : Event
