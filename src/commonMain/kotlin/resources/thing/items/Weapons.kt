package resources.thing.items

import core.thing.item.ItemResource
import core.thing.things
import resources.thing.burnable

class Weapons : ItemResource {

    override val values = things {
        thing(burnable)

        thing("Rusty Dagger") {
            material("Iron")
            body("Dagger")
            description("It once held a cutting edge, but those days are little remembered.")
            equipSlotOptions("Right Hand Grip")
            equipSlotOptions("Left Hand Grip")
            props {
                value("weight", 1)
                value("slashDamage", 1)
                value("stabDamage", 2)
                value("range", 2)
                tag("Weapon", "Sharp", "Small")
            }
        }

        thing("Dulled Hatchet") {
            material("Iron")
            description("The handle has been polished through much use.")
            equipSlotOptions("Right Hand Grip")
            equipSlotOptions("Left Hand Grip")
            props {
                value("weight", 4)
                value("chopDamage", 4)
                value("range", 5)
                tag("Weapon", "Sharp", "Small")
            }
        }


    }

}