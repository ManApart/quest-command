package resources.target.items.apparel

import core.target.item.ItemResource
import core.target.targets
import resources.target.burnable

class Apparel : ItemResource {

    override val values = targets {
        target("Brown Pants") {
            extends(burnable)
            description("These pants used to be white.")
            param("fireHealth" to 1)
            param("itemName" to "Brown Pants")
            props {
                value("weight", 1)
                value("defense", 1)
            }
            equipSlot("Waist", "Right Leg", "Left Leg")
        }

        target("Old Shirt") {
            extends(burnable)
            description("A faint scent of soil oozes from the worn cloth.")
            param("fireHealth" to 1)
            param("itemName" to "Old Shirt")
            props {
                value("weight", 1)
                value("defense", 1)
            }
            equipSlot("Chest")
        }

        target("Small Pouch") {
            description("A pouch for storing small items.")
            param("fireHealth" to 1)
            param("itemName" to "Brown Pants")
            props {
                tag("Open", "Container")
                value("weight", 1)
                value("defense", 1)
            }
            body("Sack"){
                part {
                    props {
                        tag("Container")
                        value("weight", 1)
                        value("defense", 1)
                    }
                }
            }
            equipSlotOptions("Belt Front", "Belt Left", "Belt Right", "Belt Back")
        }
    }
}