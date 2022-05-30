package traveling.scope.spawn

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector

class SpawnItemEvent(
    val itemName: String,
    val count: Int = 1,
    val thing: Thing? = null,
    val thingLocation: LocationNode? = null,
    position: Vector? = null,
    positionParent: Thing? = null
) : Event {
    val position = position ?: positionParent?.position ?: NO_VECTOR
}