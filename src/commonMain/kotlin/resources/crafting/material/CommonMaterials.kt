package resources.crafting.material

import crafting.material.MaterialResource
import crafting.material.MaterialStrings.ADAMANT
import crafting.material.MaterialStrings.AIR
import crafting.material.MaterialStrings.BLACKENED_STEEL
import crafting.material.MaterialStrings.BRONZE
import crafting.material.MaterialStrings.CLOTH
import crafting.material.MaterialStrings.DIRT
import crafting.material.MaterialStrings.FLESH
import crafting.material.MaterialStrings.FOOD
import crafting.material.MaterialStrings.FUR
import crafting.material.MaterialStrings.GRAVEL
import crafting.material.MaterialStrings.IRON
import crafting.material.MaterialStrings.LEATHER
import crafting.material.MaterialStrings.MITHRIL
import crafting.material.MaterialStrings.PLANT
import crafting.material.MaterialStrings.STEEL
import crafting.material.MaterialStrings.STONE
import crafting.material.MaterialStrings.WATER
import crafting.material.MaterialStrings.WOOD
import crafting.material.materials

class CommonMaterials : MaterialResource {
    override val values = materials {
        material(AIR, 0, 0)
        material(CLOTH, 25, 50)
        material(DIRT, 50, 50)
        material(FLESH, 40, 20)
        material(FOOD, 10, 20)
        material(FUR, 30, 50)
        material(PLANT, 15, 40)
        material(GRAVEL, 60, 60)
        material(LEATHER, 30, 30)
        material(WOOD, 50, 30)
        material(STONE, 70, 40)
        material(WATER, 5, 0)

        group("Metals") {
            props {
                tag("Metal")
            }

            material(BRONZE, 60, 30) {
                value("Quality", 1)
            }
            material(IRON, 70, 20) {
                value("Quality", 2)
            }
            material(STEEL, 80, 15) {
                value("Quality", 3)
            }
            material(BLACKENED_STEEL, 85, 15) {
                value("Quality", 4)
            }
            material(ADAMANT, 90, 10) {
                value("Quality", 5)
            }
            material(MITHRIL, 95, 5) {
                value("Quality", 6)
            }
        }

    }
}
