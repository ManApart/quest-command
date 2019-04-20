package interact.scope.spawn

import core.events.Event
import core.gameState.Target
import core.gameState.location.LocationNode

class ItemSpawnedEvent(val item: Target, val target: Target?, val targetLocation: LocationNode? = null) : Event