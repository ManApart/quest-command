package core.gameState.location

import core.gameState.NO_VECTOR
import core.gameState.Vector

class ProtoConnection(
        val target: String? = null,
        val part: String? = null,
        val vector: Vector = NO_VECTOR,
        name: String? = null,
        connection: ProtoTarget? = null,
        var restricted: Boolean = false,
        val oneWay: Boolean = false
) {
    val connection = connection ?: ProtoTarget(location = name!!)

}