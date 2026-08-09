package resources.body

import core.body.Body2Resource
import core.body.bodies
import crafting.material.MaterialStrings.FLESH
import crafting.material.MaterialStrings.IRON
import crafting.material.MaterialStrings.LEATHER
import crafting.material.MaterialStrings.STONE
import crafting.material.MaterialStrings.WOOD

class CommonBodies2 : Body2Resource{
    override val values = bodies {
        body("Human") {
            dimensions(6, 2, 10)
            mat(FLESH)
            parts("Head", "Right Arm", "Left Arm", "Waist", "Right Leg", "Left Leg", "Right Foot", "Left Foot")
        }
        body("Rat") {
            dimensions(2, 4, 2)
            mat(FLESH)
            parts("Canine Head", "Canine Torso", "Small Claws")
        }
        body("Tree") {
            dimensions(1, 1, 15)
            mat(WOOD)
            parts("Branches", "Trunk")
        }
        body("City Wall") {
            dimensions(100, 10, 20)
            mat(STONE)
            parts("Branches", "Trunk")
        }
        body("Wall Crack") {
            dimensions(5, 5, 10)
            mat(STONE)
            parts("Base", "Top")
        }
        body("Dagger") {
            mat(IRON)
            parts("Pommel", "Guard", "Blade")
            part("Handle") {
                mat(LEATHER)
            }
        }
    } + bodies("Stairs", "Sack", "Grain Bin", "Grain Chute", "Medium Container")
}
