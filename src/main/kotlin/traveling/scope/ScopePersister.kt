package traveling.scope

import core.target.getPersisted
import traveling.location.location.LocationNode


fun getPersisted(dataObject: Scope): Map<String, Any> {
    val data = mutableMapOf<String, Any>("version" to 1)
    data["targets"] = dataObject.getTargets().filter { !it.isPlayer() }.map { getPersisted(it) }
    return data
}

@Suppress("UNCHECKED_CAST")
fun readFromData(data: Map<String, Any>, locationNode: LocationNode): Scope {
    val targets = (data["targets"] as List<Map<String, Any>>).map {
        core.target.readFromData(it)
    }

    val scope = Scope(locationNode)
    scope.addTargets(targets)
    return scope
}