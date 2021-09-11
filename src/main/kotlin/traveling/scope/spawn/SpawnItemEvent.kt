package traveling.scope.spawn

import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector

class SpawnItemEvent(
    val itemName: String,
    val count: Int = 1,
    val target: Target? = null,
    val targetLocation: LocationNode? = null,
    position: Vector? = null,
    positionParent: Target? = null
) : Event {
    val position = position ?: positionParent?.position ?: NO_VECTOR
}