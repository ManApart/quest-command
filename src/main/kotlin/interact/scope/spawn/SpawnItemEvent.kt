package interact.scope.spawn

import core.events.Event
import core.gameState.NO_VECTOR
import core.gameState.Target
import core.gameState.Vector
import core.gameState.location.LocationNode

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