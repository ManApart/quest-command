package traveling.scope.spawn

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector

class SpawnActivatorEvent(val activator: Thing, val silent: Boolean = false, val thingLocation: LocationNode, position: Vector? = null, positionParent: Thing? = null) : Event {
    val position = position ?: positionParent?.position ?: NO_VECTOR
}