package resources.thing.items.apparel

import core.thing.item.ItemResource
import core.thing.things

class IronArmor : ItemResource {

    override suspend fun values() = things {
        thing("Iron Half Helm") {
            material("Iron")
            description("The leather padding is worn, but the iron shell should protect your skull.")
            props {
                value("weight", 5)
                value("defense", 5)
            }
            equipSlot("Head Outer")
        }

        thing("Iron Chest Plate") {
            material("Iron")
            description("Pits, dents, and scars are scattered across both front and back plates.")
            props {
                value("weight", 10)
                value("defense", 10)
            }
            equipSlot("Chest Outer", "Right Arm Outer", "Left Arm Outer")
        }

        thing("Iron Grieves") {
            material("Iron")
            description("These are as heavy as you'd expect.")
            props {
                value("weight", 10)
                value("defense", 10)
            }
            equipSlot("Waist Outer", "Right Leg Outer", "Left Leg Outer")
        }

    }
}