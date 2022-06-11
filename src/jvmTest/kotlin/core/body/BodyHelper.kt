package core.body

import traveling.location.Network
import traveling.location.location.LocationRecipe
import traveling.location.network.LocationNode

fun createBody(part: LocationRecipe) : Body {
    return createBody(listOf(part))
}

fun createBody(parts: List<LocationRecipe>) : Body {
    val name = "None"
    val nodes = parts.map { LocationNode(it.name, parent = name) }
    val network = Network(name, nodes, parts)
    network.getLocationNodes().forEach { it.network = network }
    return Body(name, network)
}