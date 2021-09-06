package core.body

import traveling.location.location.*
import traveling.location.location.BodyBuilder

class BodysMock(
    override val values: List<BodyBuilder> = listOf(body("Human"){location("Body Part")}, body("None"){location("Part")})
) : BodysCollection {
    companion object {
        fun withFakePlayer(): BodysMock {
            return BodysMock(bodies {
                body("Human") {
                    location("right hand")
                    location("left hand")
                }
            })
        }

        fun fromPart(part: LocationNodeBuilder, name: String = "body"): BodysMock {
            return fromParts(listOf(part), name)
        }

        fun fromParts(parts: List<LocationNodeBuilder>, name: String = "body"): BodysMock {
            return BodysMock(bodies {
                body(name) {
                    parts.forEach { part ->
                        location(part)
                    }
                }
            })
        }

    }
}