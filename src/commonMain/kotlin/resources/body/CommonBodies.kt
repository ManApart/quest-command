package resources.body

import core.body.BodyResource
import core.body.BodyPartStrings.CHEST
import core.body.BodyPartStrings.HEAD
import core.body.BodyPartStrings.LEFT_ARM
import core.body.BodyPartStrings.LEFT_FOOT
import core.body.BodyPartStrings.LEFT_HAND
import core.body.BodyPartStrings.LEFT_LEG
import core.body.BodyPartStrings.RIGHT_ARM
import core.body.BodyPartStrings.RIGHT_FOOT
import core.body.BodyPartStrings.RIGHT_HAND
import core.body.BodyPartStrings.RIGHT_LEG
import core.body.BodyPartStrings.WAIST
import core.body.bodies
import crafting.material.MaterialStrings.FLESH
import crafting.material.MaterialStrings.IRON
import crafting.material.MaterialStrings.LEATHER
import crafting.material.MaterialStrings.STONE
import crafting.material.MaterialStrings.WOOD

class CommonBodies : BodyResource {
    override val values = bodies {
        body("Human") {
            dimensions(6, 2, 10)
            mat(FLESH)
            parts(CHEST, HEAD, RIGHT_ARM, LEFT_ARM, RIGHT_HAND, LEFT_HAND, WAIST, RIGHT_LEG, LEFT_LEG, RIGHT_FOOT, LEFT_FOOT)
        }
        body("Rat") {
            dimensions(2, 4, 2)
            mat(FLESH)
            parts("Canine Torso", "Canine Head", "Small Claws")
        }
        body("Tree") {
            dimensions(1, 1, 15)
            mat(WOOD)
            parts("Trunk", "Branches")
        }
        body("City Wall") {
            dimensions(100, 10, 20)
            mat(STONE)
            parts("Trunk", "Branches")
        }
        body("Wall Crack") {
            dimensions(5, 5, 10)
            mat(STONE)
            parts("Base", "Top")
        }
        body("Dagger") {
            mat(IRON)
            part("Handle") {
                material(LEATHER)
            }
            parts("Pommel", "Guard", "Blade")
        }
    } + bodies("Stairs", "Sack", "Grain Bin", "Grain Chute", "Medium Container")
}
