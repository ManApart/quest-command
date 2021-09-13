package resources.target.items

import core.target.item.ItemResource
import core.target.targets

class Items : ItemResource {

    override val values = targets {

        target("Tinder Box") {
            description("This can light flammable things on fire.")
            props {
                value("weight", 1)
                tag("Fire Starter")
            }
        }

        target("Ash") {
            description("The result of a fire's unconstrained hunger.")
        }

        target("Pie Tin") {
            description("The tin ring is slightly bronzed from repeated trips through fire.")
            props {
                value("weight", 1)
            }
        }

        target("Bucket") {
            description("Any empty bucket that can be filled with liquids. Avoid kicking it.")
            props {
                value("weight", 1)
            }
        }

        target("Bucket of Water") {
            description("Looks clean enough.")
            props {
                value("weight", 2)
            }
        }

        target("Pot") {
            description("An empty pot that can be filled with fine grained solids.")
            props {
                value("weight", 1)
            }
        }

        target("Apple Pie Recipe") {
            description("It's a recipe to make Apple Pie")
            props {
                value("weight", 0)
            }
            behavior("Learn Recipe", "recipe" to "Apple Pie")
        }

        target("Lantern") {
            description("The metal cage is battered, and the glass seems to seep down towards the base.")
            props {
                value("fireHealth", 2)
                value("litLight", 2)
                tag("Metal", "Flammable", "Light")
            }
            behavior("Burn Out", "fireHealth" to 5)
        }

    }

}