package core.gamestate.body

import core.body.Body
import core.body.BodyPart
import traveling.location.location.Location
import traveling.location.location.LocationNode
import traveling.location.Network

fun createBody(part: BodyPart) : Body {
    return createBody(listOf(part))
}

fun createBody(parts: List<BodyPart>) : Body {
    val name = "None"
    val nodes = parts.map { LocationNode(name, parent = name) }
    val locations = parts.map { Location(name, bodyPart = it) }
    return Body(name, Network(name, nodes, locations))
}