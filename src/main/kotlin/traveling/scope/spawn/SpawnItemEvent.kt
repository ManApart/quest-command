package traveling.scope.spawn

import core.events.Event
import traveling.direction.NO_VECTOR
import core.target.Target
import traveling.direction.Vector
import traveling.location.LocationNode

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