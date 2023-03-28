package traveling.scope.spawn

import core.events.Event
import core.thing.Thing
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector

data class SpawnItemEvent(
    val itemName: String,
    val count: Int = 1,
    val thing: Thing? = null,
    val thingLocation: LocationNode? = null,
    val position: Vector
) : Event {
    constructor(itemName: String, count: Int = 1, thing: Thing? = null, thingLocation: LocationNode? = null, position: Vector? = null, positionParent: Thing? = null) : this(
        itemName, count, thing, thingLocation, position ?: positionParent?.position ?: NO_VECTOR
    )
}