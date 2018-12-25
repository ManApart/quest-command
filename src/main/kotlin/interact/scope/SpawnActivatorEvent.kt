package interact.scope

import core.events.Event
import core.gameState.Activator
import core.gameState.location.LocationNode

class SpawnActivatorEvent(val activator: Activator, val silent: Boolean = false, val targetLocation: LocationNode? = null) : Event