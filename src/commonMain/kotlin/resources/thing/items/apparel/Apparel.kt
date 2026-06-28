package resources.thing.items.apparel

import core.properties.DEFENSE
import core.properties.ParameterStrings.FIRE_HEALTH
import core.properties.ParameterStrings.ITEM_NAME
import core.properties.TagStrings.CONTAINER
import core.properties.TagStrings.OPEN
import core.properties.ValueStrings.WEIGHT
import core.thing.item.ItemResource
import core.thing.things
import crafting.material.MaterialStrings.CLOTH
import crafting.material.MaterialStrings.LEATHER
import resources.thing.burnable

class Apparel : ItemResource {

    override suspend fun values() = things {
        thing("Brown Pants") {
            extends(burnable.get())
            material(CLOTH)
            description("These pants used to be white.")
            param(FIRE_HEALTH to 1)
            param(ITEM_NAME to "Brown Pants")
            props {
                value(WEIGHT, 1)
                value(DEFENSE, 1)
            }
            equipSlot("Waist", "Right Leg", "Left Leg")
        }

        thing("Old Shirt") {
            extends(burnable.get())
            material(CLOTH)
            description("A faint scent of soil oozes from the worn cloth.")
            param(FIRE_HEALTH to 1)
            param(ITEM_NAME to "Old Shirt")
            props {
                value(WEIGHT, 1)
                value(DEFENSE, 1)
            }
            equipSlot("Chest")
        }

        thing("Small Pouch") {
            material(LEATHER)
            description("A pouch for storing small items.")
            param(FIRE_HEALTH to 1)
            param(ITEM_NAME to "Small Pouch")
            props {
                tag(OPEN, CONTAINER)
                value(WEIGHT, 1)
                value(DEFENSE, 1)
            }
            body("Sack")
            equipSlotOptions("Belt Front", "Belt Left", "Belt Right", "Belt Back")
        }
    }
}
