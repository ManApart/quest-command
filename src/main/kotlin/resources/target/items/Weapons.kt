package resources.target.items

import core.target.item.ItemResource
import core.target.targets
import resources.target.burnable

class Weapons : ItemResource {

    override val values = targets {
        target(burnable)

        target("Rusty Dagger") {
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

        target("Dulled Hatchet") {
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