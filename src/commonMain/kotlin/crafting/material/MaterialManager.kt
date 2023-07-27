package crafting.material

import building.ModManager
import core.DependencyInjector
import core.startupLog
import core.utility.lazyM

object MaterialManager {
    private var materials by lazyM { loadMaterials() }

    private fun loadMaterials(): Map<String, Material> {
        startupLog("Loading Materials")
        return (listOf(DEFAULT_MATERIAL) + DependencyInjector.getImplementation(MaterialsCollection::class).values + ModManager.materials).associateBy { it.name }
    }

    fun reset() {
        materials = loadMaterials()
    }

    fun getMaterial(name: String): Material {
        return materials[name] ?: DEFAULT_MATERIAL.also {
            println("Could not find material $name. Using Default Material '${DEFAULT_MATERIAL.name}'.")
        }
    }

    fun getAllMaterials(): Map<String, Material> {
        return materials
    }

}