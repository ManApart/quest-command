package interact.scope.spawn

import core.events.Event
import core.gameState.Target
import core.gameState.location.LocationNode

class SpawnActivatorEvent(val activator: Target, val silent: Boolean = false, val targetLocation: LocationNode? = null) : Event