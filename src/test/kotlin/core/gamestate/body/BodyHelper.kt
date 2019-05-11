package core.gamestate.body

import core.gameState.body.Body
import core.gameState.body.BodyPart
import core.gameState.location.Location
import core.gameState.location.LocationNode
import core.gameState.location.Network

fun createBody(part: BodyPart) : Body {
    return createBody(listOf(part))
}

fun createBody(parts: List<BodyPart>) : Body {
    val name = "None"
    val nodes = parts.map { LocationNode(name, parent = name) }
    val locations = parts.map { Location(name, bodyPart = it) }
    return Body(name, Network(name, nodes, locations))
}