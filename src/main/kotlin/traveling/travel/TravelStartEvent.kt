package traveling.travel

import core.GameState
import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode

class TravelStartEvent(val creature: Target, val currentLocation: LocationNode = creature.location, val destination: LocationNode, val quiet: Boolean = false) : Event