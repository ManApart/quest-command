package resources.crafting.material

import crafting.material.MaterialResource
import crafting.material.materials

class CommonMaterials : MaterialResource {
    override val values = materials {
        material("Air", 0, 0)
        material("Dirt", 50, 50)
        material("Gravel", 60, 60)
        material("Gem", 80, 5)
        material("Iron", 80, 20)
        material("Leather", 30, 30)
        material("Wood", 50, 30)
        material("Bark", 30, 60)
        material("Stone", 70, 40)
        material("Water", 20, 0)
    }
}