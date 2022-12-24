package crafting.material

import core.DependencyInjector
import core.startupLog
import core.utility.NameSearchableList
import core.utility.lazyM
import core.utility.toNameSearchableList
import crafting.Recipe
import crafting.RecipesCollection
import crafting.build

object MaterialManager {
    private var materials by lazyM { loadMaterials() }

    private fun loadMaterials(): Map<String, Material> {
        startupLog("Loading Materials")
        return DependencyInjector.getImplementation(MaterialsCollection::class).values.associateBy { it.name }
    }

    fun reset() {
        materials = loadMaterials()
    }

    fun getMaterial(name: String): Material {
        return materials[name] ?: DEFAULT_MATERIAL.also {
            println("Could not find material $name. Using Default Material")
        }
    }

    fun getAllMaterials(): Map<String, Material> {
        return materials
    }

}