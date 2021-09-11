package traveling.scope.spawn

import core.GameState
import core.events.Event
import core.target.Target
import traveling.location.network.LocationNode
import traveling.position.NO_VECTOR
import traveling.position.Vector

class SpawnActivatorEvent(val activator: Target, val silent: Boolean = false, val targetLocation: LocationNode? = GameState.player.location, position: Vector? = null, positionParent: Target? = null) : Event {
    val position = position ?: positionParent?.position ?: NO_VECTOR
}