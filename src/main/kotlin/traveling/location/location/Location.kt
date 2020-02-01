package traveling.location.location

import core.body.BodyPart
import core.properties.Properties
import core.utility.Named
import dialogue.DialogueOptions

val NOWHERE = Location("Nowhere")

class Location(
        override val name: String,
        val description: String = "",
        val activators: List<LocationTarget> = listOf(),
        val creatures: List<LocationTarget> = listOf(),
        val items: List<LocationTarget> = listOf(),
        var bodyPart: BodyPart? = null,
        private val weatherSelector: DialogueOptions = DialogueOptions("Still"),
        val properties: Properties = Properties()
) : Named {
    constructor(base: Location) : this(
            base.name,
            base.description,
            base.activators.toList(),
            base.creatures.toList(),
            base.items.toList(),
            if (base.bodyPart != null) {
                BodyPart(base.bodyPart!!)
            } else {
                null
            },
            base.weatherSelector,
            Properties(base.properties)
    )

    override fun toString(): String {
        return name
    }

    fun getWeatherName() : String {
        return weatherSelector.getDialogue()
    }

}