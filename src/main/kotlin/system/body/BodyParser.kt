package system.body

import core.gameState.body.BodyPart
import core.gameState.location.LocationNode

interface BodyParser {
    fun loadBodyParts(): List<BodyPart>
    fun loadBodies(): List<LocationNode>
}