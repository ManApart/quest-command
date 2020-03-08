package system

import core.utility.NameSearchableList
import traveling.location.location.Location
import traveling.location.location.LocationNode
import traveling.location.location.LocationParser

class BodyFakeParser(
        private val protoBodies: List<LocationNode> = listOf(LocationNode(parent = "Human", name = "Body Part"), LocationNode(parent = "None", name = "Part")),
        private val bodyParts: List<Location> = listOf(Location("Body Part"))
) : LocationParser {
    override fun loadLocations(): NameSearchableList<Location> {
        return NameSearchableList(bodyParts)
    }

    override fun loadLocationNodes(): List<LocationNode> {
        return protoBodies
    }

    companion object {
        fun parserWithFakePlayer(): BodyFakeParser {
            return BodyFakeParser(listOf(
                    LocationNode(parent = "Human", name = "right hand", locationName = "right hand"),
                    LocationNode(parent = "Human", name = "left hand", locationName = "left hand")
            ),
                    listOf(Location("right hand"), Location("left hand")))
        }

        fun parserFromPart(part: Location, name: String = "body"): BodyFakeParser {
            return parserFromParts(listOf(part), name)
        }

        fun parserFromParts(parts: List<Location>, name: String = "body"): BodyFakeParser {
            val nodes = parts.map { LocationNode(it.name, parent = name) }
            return BodyFakeParser(nodes, parts)
        }

    }
}
