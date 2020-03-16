package system

import core.utility.NameSearchableList
import traveling.location.location.LocationRecipe
import traveling.location.location.LocationNode
import traveling.location.location.LocationParser

class BodyFakeParser(
        private val protoBodies: List<LocationNode> = listOf(LocationNode(parent = "Human", name = "Body Part"), LocationNode(parent = "None", name = "Part")),
        private val bodyParts: List<LocationRecipe> = listOf(LocationRecipe("Body Part"))
) : LocationParser {
    override fun loadLocations(path: String): NameSearchableList<LocationRecipe> {
        return NameSearchableList(bodyParts)
    }

    override fun loadLocationNodes(path: String): List<LocationNode> {
        return protoBodies
    }

    companion object {
        fun parserWithFakePlayer(): BodyFakeParser {
            return BodyFakeParser(listOf(
                    LocationNode(parent = "Human", name = "right hand", locationName = "right hand"),
                    LocationNode(parent = "Human", name = "left hand", locationName = "left hand")
            ),
                    listOf(LocationRecipe("right hand"), LocationRecipe("left hand")))
        }

        fun parserFromPart(part: LocationRecipe, name: String = "body"): BodyFakeParser {
            return parserFromParts(listOf(part), name)
        }

        fun parserFromParts(parts: List<LocationRecipe>, name: String = "body"): BodyFakeParser {
            val nodes = parts.map { LocationNode(it.name, parent = name) }
            return BodyFakeParser(nodes, parts)
        }

    }
}
