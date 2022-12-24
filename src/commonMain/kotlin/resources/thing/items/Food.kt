package resources.thing.items

import core.thing.item.ItemResource
import core.thing.things
import resources.thing.burnable

class Food : ItemResource {

    override val values = things {
        thing("Apple") {
            material("Food")
            description("It glistens and smells of spring blossoms.")
            props {
                value("healAmount", 1)
                value("weight", 1)
                tag("Raw", "Food", "Fruit", "Slicable")
            }
        }

        thing("Apple Pie") {
            material("Food")
            description("Its golden brown crust smells delicious.")
            props {
                value("healAmount", 10)
                value("weight", 1)
                tag("Food")
            }
            behavior("Add on Eat", "resultItemName" to "Pie Tin")
        }

        thing("Wheat Bundle") {
            material("Food")
            description("It's stems lightly scratch your hands.")
            extends(burnable)
            param("fireHealth", 1)
            param("itemName", "Wheat Bundle")
            props {
                value("healAmount", 1)
                value("weight", 1)
                tag("Raw", "Food", "Fruit", "Slicable")
            }
        }

        thing("Wheat Flour") {
            material("Food")
            description("The finely ground wheat looks almost like silver sand.")
            props {
                value("weight", 1)
                tag("Grounded")
            }
        }

        thing("Dough") {
            material("Food")
            description("It may not be of much value alone, but it forms the base of many a great meal.")
            props {
                value("weight", 1)
            }
        }

        thing("Poor Quality Meat") {
            material("Flesh")
            description("It's pale and oily.")
            props {
                value("weight", 1)
                tag("Raw", "Meat", "Slicable")
            }

        }


    }

}