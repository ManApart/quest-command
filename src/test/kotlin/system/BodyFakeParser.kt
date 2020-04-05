package system

import core.utility.NameSearchableList
import traveling.location.location.LocationRecipe
import traveling.location.location.LocationNode
import traveling.location.location.LocationParser

class BodyFakeParser(
        private val protoBodies: List<LocationNode> = listOf(LocationNode(name = "Body Part", parent = "Human"), LocationNode(name = "Part", parent = "None")),
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
                    LocationNode(name = "right hand", locationName = "right hand", parent = "Human"),
                    LocationNode(name = "left hand", locationName = "left hand", parent = "Human")
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
