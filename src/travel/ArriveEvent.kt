package travel

import core.events.Event
import core.gameState.Location

class ArriveEvent(val destination: Location)  : Event {
}