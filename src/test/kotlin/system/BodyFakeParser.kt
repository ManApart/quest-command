package system

import core.gameState.body.BodyPart
import core.gameState.location.Location
import core.gameState.location.LocationNode
import system.body.BodyParser

class BodyFakeParser(
        private val protoBodies: List<LocationNode> = listOf(LocationNode(parent = "Human", name = "Body Part"), LocationNode(parent = "None", name = "Part")),
        private val bodyParts: List<BodyPart> = listOf(BodyPart("Body Part"))
) : BodyParser {
    override fun loadBodyParts(): List<BodyPart> {
        return bodyParts
    }

    override fun loadBodies(): List<LocationNode> {
        return protoBodies
    }

    companion object {
        fun parserWithFakePlayer(): BodyFakeParser {
            return BodyFakeParser(listOf(
                    LocationNode(parent = "Human", name = "right hand", locationName = "right hand"),
                    LocationNode(parent = "Human", name = "left hand", locationName = "left hand")
            ),
                    listOf(BodyPart("right hand"), BodyPart("left hand")))
        }

        fun parserFromPart(part: BodyPart, name: String = "body"): BodyFakeParser {
            return parserFromParts(listOf(part), name)
        }

        fun parserFromParts(parts: List<BodyPart>, name: String = "body"): BodyFakeParser {
            val nodes = parts.map { LocationNode(it.name, parent = name) }
            return BodyFakeParser(nodes, parts)
        }

    }
}
