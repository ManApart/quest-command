package resources.target.items

import core.target.item.ItemResource
import core.target.targets
import resources.target.burnable

class Food : ItemResource {

    override val values = targets {
        target("Apple") {
            description("It glistens and smells of spring blossoms.")
            props {
                value("healAmount", 1)
                value("weight", 1)
                tag("Raw", "Food", "Fruit", "Slicable")
            }
        }

        target("Apple Pie") {
            description("Its golden brown crust smells delicious.")
            props {
                value("healAmount", 10)
                value("weight", 1)
                tag("Food")
            }
            behavior("Add on Eat", "resultItemName" to "Pie Tin")
        }

        target("Wheat Bundle") {
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

        target("Wheat Flour") {
            description("The finely ground wheat looks almost like silver sand.")
            props {
                value("weight", 1)
                tag("Grounded")
            }
        }

        target("Dough") {
            description("It may not be of much value alone, but it forms the base of many a great meal.")
            props {
                value("weight", 1)
            }
        }

        target("Poor Quality Meat") {
            description("It's pale and oily.")
            props {
                value("weight", 1)
                tag("Raw", "Meat", "Slicable")
            }

        }


    }

}