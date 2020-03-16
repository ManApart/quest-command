package core.body

import traveling.location.location.LocationRecipe
import traveling.location.location.LocationNode
import traveling.location.Network

fun createBody(part: LocationRecipe) : Body {
    return createBody(listOf(part))
}

fun createBody(parts: List<LocationRecipe>) : Body {
    val name = "None"
    val nodes = parts.map { LocationNode(name, parent = name) }
    return Body(name, Network(name, nodes, parts))
}