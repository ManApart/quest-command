package traveling.scope.spawn

import core.GameState
import core.events.Event
import traveling.position.NO_VECTOR
import core.target.Target
import traveling.position.Vector
import traveling.location.location.LocationNode

class SpawnActivatorEvent(val activator: Target, val silent: Boolean = false, val targetLocation: LocationNode? = GameState.player.location, position: Vector? = null, positionParent: Target? = null) : Event {
    val position = position ?: positionParent?.position ?: NO_VECTOR
}