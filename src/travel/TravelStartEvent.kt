package travel

import core.events.Event
import core.gameState.Location

class TravelStartEvent(val currentLocation: Location, val destination: Location) : Event