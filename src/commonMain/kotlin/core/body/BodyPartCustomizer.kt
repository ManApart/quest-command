package core.body

import crafting.material.MaterialManager
import traveling.location.location.Location

class BodyPartCustomizer {
    private val customizations = mutableListOf<Location.() -> Unit>()

    fun change(initializer: Location.() -> Unit) {
        customizations.add(initializer)
    }

    fun material(name: String) {
        customizations.add { material = MaterialManager.getMaterial(name) }
    }

    fun apply(base: Location) {
        customizations.forEach { base.apply(it) }
    }

}