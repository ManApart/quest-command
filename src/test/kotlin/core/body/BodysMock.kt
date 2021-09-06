package core.body

import traveling.location.location.*
import traveling.location.location.BodyBuilder

class BodysMock(
    override val values: List<BodyBuilder> = listOf(body("Human"){locationNode("Body Part")}, body("None"){locationNode("Part")})
) : BodysCollection {
    companion object {
        fun withFakePlayer(): BodysMock {
            return BodysMock(bodies {
                body("Human") {
                    locationNode("right hand")
                    locationNode("left hand")
                }
            })
        }

        fun fromPart(part: LocationRecipeBuilder, name: String = "body"): BodysMock {
            return fromParts(listOf(part), name)
        }

        fun fromParts(parts: List<LocationRecipeBuilder>, name: String = "body"): BodysMock {
            return BodysMock(bodies {
                body(name) {
                    parts.forEach { part ->
                        locationNode(part.name)
                    }
                }
            })
        }
    }
}