package core.body

import traveling.location.location.LocationRecipeBuilder
import traveling.location.network.NetworkBuilder
import traveling.location.network.network
import traveling.location.network.networks

class BodysMock(
    override val values: List<NetworkBuilder> = listOf(network("Human"){locationNode("Body Part")}, network("None"){locationNode("Part")})
) : BodysCollection {
    companion object {
        fun withFakePlayer(): BodysMock {
            return BodysMock(networks {
                network("Human") {
                    locationNode("right hand")
                    locationNode("left hand")
                }
            })
        }

        fun fromPart(part: LocationRecipeBuilder, name: String = "body"): BodysMock {
            return fromParts(listOf(part), name)
        }

        fun fromParts(parts: List<LocationRecipeBuilder>, name: String = "body"): BodysMock {
            return BodysMock(networks {
                network(name) {
                    parts.forEach { part ->
                        locationNode(part.name)
                    }
                }
            })
        }
    }
}