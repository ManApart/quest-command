package system.body

import core.gameState.body.BodyPart
import core.gameState.body.ProtoBody

interface BodyParser {
    fun loadBodyParts(): List<BodyPart>
    fun loadBodies(): List<ProtoBody>
}