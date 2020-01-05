package traveling.travel

import core.events.Event
import core.GameState
import core.target.Target
import traveling.location.location.LocationNode

class TravelStartEvent(val creature: Target = GameState.player, val currentLocation: LocationNode = GameState.player.location, val destination: LocationNode, val quiet: Boolean = false) : Event