package resources.thing.items.apparel

import core.thing.item.ItemResource
import core.thing.things
import resources.thing.burnable

class Apparel : ItemResource {

    override suspend fun values() = things {
        thing("Brown Pants") {
            extends(burnable.get())
            material("Cloth")
            description("These pants used to be white.")
            param("fireHealth" to 1)
            param("itemName" to "Brown Pants")
            props {
                value("weight", 1)
                value("defense", 1)
            }
            equipSlot("Waist", "Right Leg", "Left Leg")
        }

        thing("Old Shirt") {
            extends(burnable.get())
            material("Cloth")
            description("A faint scent of soil oozes from the worn cloth.")
            param("fireHealth" to 1)
            param("itemName" to "Old Shirt")
            props {
                value("weight", 1)
                value("defense", 1)
            }
            equipSlot("Chest")
        }

        thing("Small Pouch") {
            material("Leather")
            description("A pouch for storing small items.")
            param("fireHealth" to 1)
            param("itemName" to "Brown Pants")
            props {
                tag("Open", "Container")
                value("weight", 1)
                value("defense", 1)
            }
            body("Sack")
            equipSlotOptions("Belt Front", "Belt Left", "Belt Right", "Belt Back")
        }
    }
}