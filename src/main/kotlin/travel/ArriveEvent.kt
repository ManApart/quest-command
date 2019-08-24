package travel

import core.events.Event
import core.gameState.Target
import core.gameState.GameState
import core.gameState.location.LocationPoint

class ArriveEvent(val creature: Target = GameState.player, val origin: LocationPoint = LocationPoint(GameState.player.location), val destination: LocationPoint, val method: String, val quiet: Boolean = false, val silent: Boolean = false) : Event
