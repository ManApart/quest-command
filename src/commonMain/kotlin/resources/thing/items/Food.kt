package resources.thing.items

import core.properties.ValueKey.HEAL_AMOUNT
import core.properties.ValueKey.WEIGHT
import core.thing.item.ItemResource
import core.thing.things
import crafting.material.MaterialStrings.FOOD
import resources.thing.burnable

class Food : ItemResource {

    override suspend fun values() = things {
        thing("Apple") {
            material(FOOD)
            description("It glistens and smells of spring blossoms.")
            props {
                value(HEAL_AMOUNT, 1)
                value(WEIGHT, 1)
                tag("Raw", FOOD, "Fruit", "Slicable")
            }
        }

        thing("Apple Pie") {
            material(FOOD)
            description("Its golden brown crust smells delicious.")
            props {
                value(HEAL_AMOUNT, 10)
                value(WEIGHT, 1)
                tag(FOOD)
            }
            behavior("Add on Eat", "resultItemName" to "Pie Tin")
        }

        thing("Wheat Bundle") {
            material(FOOD)
            description("It's stems lightly scratch your hands.")
            extends(burnable.get())
            param("fireHealth", 1)
            param("itemName", "Wheat Bundle")
            props {
                value(HEAL_AMOUNT, 1)
                value(WEIGHT, 1)
                tag("Raw", FOOD, "Fruit", "Slicable")
            }
        }

        thing("Wheat Flour") {
            material(FOOD)
            description("The finely ground wheat looks almost like silver sand.")
            props {
                value(WEIGHT, 1)
                tag("Grounded")
            }
        }

        thing("Dough") {
            material(FOOD)
            description("It may not be of much value alone, but it forms the base of many a great meal.")
            props {
                value(WEIGHT, 1)
            }
        }

        thing("Poor Quality Meat") {
            material("Flesh")
            description("It's pale and oily.")
            props {
                value(WEIGHT, 1)
                tag("Raw", "Meat", "Slicable")
            }

        }


    }

}