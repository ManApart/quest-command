package traveling.scope.remove

import core.events.Event
import core.target.Target
import traveling.location.LocationNode

class RemoveScopeEvent(val target: Target, val targetLocation: LocationNode? = null) : Event