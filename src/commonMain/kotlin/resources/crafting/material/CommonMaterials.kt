package resources.crafting.material

import crafting.material.MaterialResource
import crafting.material.materials

class CommonMaterials : MaterialResource {
    override val values = materials {
        material("Air", 0, 0)
        material("Bark", 30, 60)
        material("Cloth", 25, 50)
        material("Dirt", 50, 50)
        material("Flesh", 40, 20)
        material("Food", 10, 20)
        material("Fur", 30, 50)
        material("Plant", 15, 40)
        material("Gravel", 60, 60)
        material("Leather", 30, 30)
        material("Wood", 50, 30)
        material("Stone", 70, 40)
        material("Water", 5, 0)

        group("Gems") {
            props {
                tag("Gem")
            }
            material("Topaz", 80, 5) {
                value("Quality", 1)
            }
            material("Sapphire", 80, 5) {
                value("Quality", 2)
            }
            material("Diamond", 100, 5) {
                value("Quality", 3)
            }
        }

        group("Metals") {
            props {
                tag("Metal")
            }

            material("Bronze", 60, 30) {
                value("Quality", 1)
            }
            material("Iron", 70, 20) {
                value("Quality", 2)
            }
            material("Steel", 80, 15) {
                value("Quality", 3)
            }
            material("Blackened Steel", 85, 15) {
                value("Quality", 4)
            }
            material("Adamant", 90, 10) {
                value("Quality", 5)
            }
            material("Mithril", 95, 5) {
                value("Quality", 6)
            }
        }

    }
}