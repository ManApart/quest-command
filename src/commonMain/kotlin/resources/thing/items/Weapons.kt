package resources.thing.items

import core.properties.TagStrings.SMALL
import core.thing.item.ItemResource
import core.thing.things
import crafting.material.MaterialStrings
import crafting.material.MaterialStrings.IRON
import resources.thing.burnable

class Weapons : ItemResource {

    override suspend fun values() = things {
        thing(burnable.get())

        thing("Rusty Dagger") {
            body("Dagger")
            description("It once held a cutting edge, but those days are little remembered.")
            equipToHoldOneHand()
            props {
                value("weight", 1)
                value("slashDamage", 1)
                value("stabDamage", 2)
                value("range", 2)
                tag("Weapon", "Sharp", SMALL)
            }
        }
        thing("Broken Dagger") {
            extends("Rusty Dagger")
            description("The blade has broken off.")
            body {
                removePart("Blade")
                part("Broken Blade")
            }
            props {
                value("stabDamage", 1)
                value("range", 1)
            }
        }
        thing("Silver Dagger") {
            extends("Rusty Dagger")
            description("Polished to a sheen, the silver blade reflects your gaze.")
            body {
                part("Blade", MaterialStrings.SILVER)
            }
            props {
                value("stabDamage", 3)
            }
        }

        thing("Dulled Hatchet") {
            material(IRON)
            description("The handle has been polished through much use.")
            equipToHoldOneHand()
            props {
                value("weight", 4)
                value("chopDamage", 4)
                value("range", 5)
                tag("Weapon", "Sharp", SMALL)
            }
        }
    }
}
