package resources.target.items.apparel

import core.target.item.ItemResource
import core.target.targets

class IronArmor : ItemResource {

    override val values = targets {
        target("Iron Half Helm") {
            description("The leather padding is worn, but the iron shell should protect your skull.")
            props {
                value("weight", 5)
                value("defense", 5)
            }
            equipSlot("Head Outer")
        }

        target("Iron Chest Plate") {
            description("Pits, dents, and scars are scattered across both front and back plates.")
            props {
                value("weight", 10)
                value("defense", 10)
            }
            equipSlot("Chest Outer", "Right Arm Outer", "Left Arm Outer")
        }

        target("Iron Grieves") {
            description("These are as heavy as you'd expect.")
            props {
                value("weight", 10)
                value("defense", 10)
            }
            equipSlot("Waist Outer", "Right Leg Outer", "Left Leg Outer")
        }

    }
}