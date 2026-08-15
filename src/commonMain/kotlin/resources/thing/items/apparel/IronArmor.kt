package resources.thing.items.apparel

import core.body.BodyPartStrings.CHEST
import core.body.BodyPartStrings.HEAD
import core.body.BodyPartStrings.LEFT_ARM
import core.body.BodyPartStrings.RIGHT_ARM
import core.body.EquipLayerStrings.ARMOR
import core.thing.item.ItemResource
import core.thing.things
import crafting.material.MaterialStrings.IRON

class IronArmor : ItemResource {

    override suspend fun values() = things {
        thing("Iron Half Helm") {
            material(IRON)
            description("The leather padding is worn, but the iron shell should protect your skull.")
            props {
                value("weight", 5)
                value("defense", 5)
            }
            equipTo(ARMOR, HEAD)
        }

        thing("Iron Chest Plate") {
            material(IRON)
            description("Pits, dents, and scars are scattered across both front and back plates.")
            props {
                value("weight", 10)
                value("defense", 10)
            }
            equipTo(ARMOR, CHEST, RIGHT_ARM, LEFT_ARM)
        }

        thing("Iron Grieves") {
            material(IRON)
            description("These are as heavy as you'd expect.")
            props {
                value("weight", 10)
                value("defense", 10)
            }
            equipArmorPants()
        }

    }
}
