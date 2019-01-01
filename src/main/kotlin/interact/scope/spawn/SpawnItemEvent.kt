package interact.scope.spawn

import core.events.Event
import core.gameState.Creature
import core.gameState.location.LocationNode

class SpawnItemEvent(val itemName: String, val count: Int = 1, val target: Creature? = null, val targetLocation: LocationNode? = null) : Event