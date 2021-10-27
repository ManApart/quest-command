package resources.thing.items

import core.thing.item.ItemResource
import core.thing.things
import traveling.scope.LIT_LIGHT

class Items : ItemResource {

    override val values = things {

        thing("Tinder Box") {
            description("This can light flammable things on fire.")
            props {
                value("weight", 1)
                tag("Fire Starter")
            }
        }

        thing("Ash") {
            description("The result of a fire's unconstrained hunger.")
        }

        thing("Pie Tin") {
            description("The tin ring is slightly bronzed from repeated trips through fire.")
            props {
                value("weight", 1)
            }
        }

        thing("Bucket") {
            description("Any empty bucket that can be filled with liquids. Avoid kicking it.")
            props {
                value("weight", 1)
            }
        }

        thing("Bucket of Water") {
            description("Looks clean enough.")
            props {
                value("weight", 2)
            }
        }

        thing("Pot") {
            description("An empty pot that can be filled with fine grained solids.")
            props {
                value("weight", 1)
            }
        }

        thing("Apple Pie Recipe") {
            description("It's a recipe to make Apple Pie")
            props {
                value("weight", 0)
            }
            behavior("Learn Recipe", "recipe" to "Apple Pie")
        }

        thing("Lantern") {
            description("The metal cage is battered, and the glass seems to seep down towards the base.")
            props {
                value("fireHealth", 2)
                value(LIT_LIGHT, 7)
                tag("Metal", "Flammable", "Light")
            }
            behavior("Burn Out", "fireHealth" to 5)
        }

    }

}