package resources.target.items

import core.target.item.ItemResource
import core.target.targets
import resources.target.burnToAsh
import resources.target.burnable

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


    }

}