package explore.map

import core.events.Event
import core.gameState.Location

class MapEvent(val target: Location, val type: Type) : Event {
    enum class Type {INFO, CHILDREN, SIBLINGS }
}