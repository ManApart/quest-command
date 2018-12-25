package interact.scope

import core.events.Event
import core.gameState.Target
import core.gameState.location.LocationNode

class RemoveScopeEvent(val target: Target, val targetLocation: LocationNode? = null) : Event