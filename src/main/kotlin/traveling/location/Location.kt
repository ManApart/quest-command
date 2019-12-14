package traveling.location

import core.properties.Properties
import core.body.BodyPart
import core.utility.Named

val NOWHERE = Location("Nowhere")

class Location(
        override val name: String,
        val description: String = "",
        val activators: List<LocationTarget> = listOf(),
        val creatures: List<LocationTarget> = listOf(),
        val items: List<LocationTarget> = listOf(),
        val bodyPart: BodyPart? = null,
        val properties: Properties = Properties()
) : Named {
    constructor(base: Location) : this(
            base.name,
            base.description,
            base.activators.toList(),
            base.creatures.toList(),
            base.items.toList(),
            if (base.bodyPart != null) {
                BodyPart(base.bodyPart)
            } else {
                null
            },
            Properties(base.properties)
    )

    override fun toString(): String {
        return name
    }

}