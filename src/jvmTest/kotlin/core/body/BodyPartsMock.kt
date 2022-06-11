package core.body
import traveling.location.location.LocationRecipeBuilder
import traveling.location.location.locations

class BodyPartsMock(override val values: List<LocationRecipeBuilder> = listOf()) : BodyPartsCollection {
    companion object {
        fun withFakePlayer(): BodyPartsMock {
            return BodyPartsMock(locations {
                location("right hand")
                location("left hand")
            })
        }

        fun fromPart(part: LocationRecipeBuilder): BodyPartsMock {
            return fromParts(listOf(part))
        }

        fun fromParts(parts: List<LocationRecipeBuilder>): BodyPartsMock {
            return BodyPartsMock(parts)
        }
    }
}