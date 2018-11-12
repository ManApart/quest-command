package explore.map

import core.events.Event
import core.gameState.location.LocationNode

class ReadMapEvent(val target: LocationNode) : Event