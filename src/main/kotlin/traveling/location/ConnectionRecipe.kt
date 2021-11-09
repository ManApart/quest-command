package traveling.location

import traveling.position.NO_VECTOR
import traveling.position.Vector

class ConnectionRecipe(
        val thing: String? = null,
        val part: String? = null,
        val originPoint: Vector = NO_VECTOR,
        val destinationPoint: Vector = NO_VECTOR,
        name: String? = null,
        connection: ConnectionThing? = null,
        var restricted: Boolean = false,
        val oneWay: Boolean = false,
        val hidden: Boolean = false
) {
    val connection = connection ?: ConnectionThing(location = name!!)

}