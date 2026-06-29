package resources.thing.items

import core.properties.TagStrings.SMALL
import core.thing.item.ItemResource
import core.thing.things
import crafting.material.MaterialStrings.BRONZE
import crafting.material.MaterialStrings.CLOTH
import crafting.material.MaterialStrings.DIRT
import crafting.material.MaterialStrings.IRON
import crafting.material.MaterialStrings.LEATHER
import crafting.material.MaterialStrings.WOOD
import traveling.scope.LIT_LIGHT

class Items : ItemResource {

    override suspend fun values() = things {

        thing("Tinder Box") {
            material(CLOTH)
            description("This can light flammable things on fire.")
            props {
                value("weight", 1)
                tag("Fire Starter")
            }
        }

        thing("Ash") {
            material(DIRT)
            description("The result of a fire's unconstrained hunger.")
        }

        thing("Pie Tin") {
            material(IRON)
            description("The tin ring is slightly bronzed from repeated trips through fire.")
            props {
                value("weight", 1)
            }
        }

        thing("Bucket") {
            material(WOOD)
            description("Any empty bucket that can be filled with liquids. Avoid kicking it.")
            props {
                value("weight", 1)
            }
        }

        thing("Bucket of Water") {
            material(WOOD)
            description("Looks clean enough.")
            props {
                value("weight", 2)
            }
        }

        thing("Pot") {
            material(DIRT)
            description("An empty pot that can be filled with fine grained solids.")
            props {
                value("weight", 1)
            }
        }

        thing("Apple Pie Recipe") {
            material(CLOTH)
            description("It's a recipe to make Apple Pie")
            props {
                value("weight", 0)
            }
            behavior("Learn Recipe", "recipe" to "Apple Pie")
        }

        thing("Lantern") {
            material(IRON)
            description("The metal cage is battered, and the glass seems to seep down towards the base.")
            props {
                value("fireHealth", 2)
                value(LIT_LIGHT, 7)
                tag("Metal", "Flammable", "Light", "Small")
            }
            behavior("Burn Out", "fireHealth" to 5)
        }

        thing("Leather") {
            material(LEATHER)
            description("It's odd to think this was once something's skin.")
            props {
                tag(SMALL)
            }
        }

        thing("Bronze Ingot") {
            material(BRONZE)
            description("A bar of pure metal, ready to be worked into something more.")
            props {
                tag("Ingot", "Small")
            }
        }

        thing("Iron Ingot") {
            material(IRON)
            description("A bar of pure metal, ready to be worked into something more.")
            props {
                tag("Ingot", "Small")
            }
        }

    }

}
