package resources.thing.items

import core.thing.item.ItemResource
import core.thing.things
import traveling.scope.LIT_LIGHT

class Items : ItemResource {

    override suspend fun values() = things {

        thing("Tinder Box") {
            material("Cloth")
            description("This can light flammable things on fire.")
            props {
                value("weight", 1)
                tag("Fire Starter")
            }
        }

        thing("Ash") {
            material("Dirt")
            description("The result of a fire's unconstrained hunger.")
        }

        thing("Pie Tin") {
            material("Iron")
            description("The tin ring is slightly bronzed from repeated trips through fire.")
            props {
                value("weight", 1)
            }
        }

        thing("Bucket") {
            material("Wood")
            description("Any empty bucket that can be filled with liquids. Avoid kicking it.")
            props {
                value("weight", 1)
            }
        }

        thing("Bucket of Water") {
            material("Wood")
            description("Looks clean enough.")
            props {
                value("weight", 2)
            }
        }

        thing("Pot") {
            material("Dirt")
            description("An empty pot that can be filled with fine grained solids.")
            props {
                value("weight", 1)
            }
        }

        thing("Apple Pie Recipe") {
            material("Cloth")
            description("It's a recipe to make Apple Pie")
            props {
                value("weight", 0)
            }
            behavior("Learn Recipe", "recipe" to "Apple Pie")
        }

        thing("Lantern") {
            material("Iron")
            description("The metal cage is battered, and the glass seems to seep down towards the base.")
            props {
                value("fireHealth", 2)
                value(LIT_LIGHT, 7)
                tag("Metal", "Flammable", "Light", "Small")
            }
            behavior("Burn Out", "fireHealth" to 5)
        }

        thing("Leather") {
            material("Leather")
            description("It's odd to think this was once something's skin.")
            props {
                tag("Small")
            }
        }

        thing("Bronze Ingot") {
            material("Bronze")
            description("A bar of pure metal, ready to be worked into something more.")
            props {
                tag("Ingot", "Small")
            }
        }

        thing("Iron Ingot") {
            material("Iron")
            description("A bar of pure metal, ready to be worked into something more.")
            props {
                tag("Ingot", "Small")
            }
        }

    }

}