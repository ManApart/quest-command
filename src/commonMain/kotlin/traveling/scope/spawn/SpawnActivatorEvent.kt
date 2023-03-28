package traveling.scope.spawn

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector

data class SpawnActivatorEvent(val activator: Thing, val silent: Boolean = false, val thingLocation: LocationNode, val position: Vector) : Event {
    constructor(activator: Thing, silent: Boolean = false, thingLocation: LocationNode, position: Vector? = null, positionParent: Thing? = null) : this(
        activator,
        silent,
        thingLocation,
        position ?: positionParent?.position ?: NO_VECTOR
    )
}