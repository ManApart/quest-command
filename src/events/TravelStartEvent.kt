package events

import gameState.Location

class TravelStartEvent(val currentLocation: Location, val destination: Location) : Event