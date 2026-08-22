package core.body

import core.body.BodyPartStrings.LEFT_HAND
import core.body.BodyPartStrings.RIGHT_HAND

class BodysMock(override val values: List<BodyBuilder> = listOf(bodyB("Human", RIGHT_HAND, LEFT_HAND), bodyB("None", "Part"))) : BodysCollection {
    companion object {
        fun fromPart(vararg parts: String): BodysMock {
            return BodysMock(listOf(bodyB("body") {
                parts(parts.toList())
            }))
        }
    }
}
