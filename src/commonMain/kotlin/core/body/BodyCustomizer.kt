package core.body

import traveling.location.Network

//Customizes an existing body initializing and updating specific body parts
//Because it's overriding location recipes, the locations must be initialized so the changes will be saved
//Used in crafting
class BodyCustomizer {
    private val parts = mutableMapOf<String, BodyPartCustomizer>()

    fun part(name: String, initializer: BodyPartCustomizer.() -> Unit = {}) {
        parts[name] = BodyPartCustomizer().apply(initializer)
    }

    fun apply(base: Body): Body {
        transform(base.layout)
        return Body(base.name, base.material, base.layout, base.getSlotMap().toMutableMap())
    }

    private fun transform(layout: Network) {
        parts.forEach { (partName, customizer) ->
            layout.getLocationNodeOrNull(partName)?.getLocation()?.let { customizer.apply(it) }
        }
    }
}