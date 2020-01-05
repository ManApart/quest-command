package traveling.scope.spawn

import core.events.Event
import core.target.Target
import traveling.location.location.LocationNode

class ItemSpawnedEvent(val item: Target, val target: Target?, val targetLocation: LocationNode? = null) : Event