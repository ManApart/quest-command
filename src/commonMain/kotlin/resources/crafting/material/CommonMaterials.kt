package resources.crafting.material

import crafting.material.MaterialResource
import crafting.material.materials

class CommonMaterials : MaterialResource {
    override val values = materials {
        material("Water"){
            density = 20
            roughness = 0

        }
    }
}