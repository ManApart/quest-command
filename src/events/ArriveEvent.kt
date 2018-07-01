package events

import gameState.Location

class ArriveEvent(val destination: Location)  : Event {
}