package system

import core.gameState.body.BodyPart
import core.gameState.body.ProtoBody
import system.body.BodyParser

class BodyFakeParser(private val protoBodies: List<ProtoBody> = listOf(), private val bodyParts: List<BodyPart> = listOf()) : BodyParser {
    override fun loadBodyParts(): List<BodyPart> {
        return bodyParts
    }

    override fun loadBodies(): List<ProtoBody> {
        return protoBodies
    }

    companion object {
        fun parserWithFakePlayer() : BodyFakeParser {
            return BodyFakeParser(listOf(ProtoBody("Human", listOf("right hand", "left hand"))), listOf(BodyPart("right hand"), BodyPart("left hand")))
        }
    }
}
