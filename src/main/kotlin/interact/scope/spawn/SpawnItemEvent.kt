package interact.scope.spawn

import core.events.Event
import core.gameState.Target
import core.gameState.location.LocationNode

class SpawnItemEvent(val itemName: String, val count: Int = 1, val target: Target? = null, val targetLocation: LocationNode? = null) : Event