package resources.thing

import core.ai.behavior.BehaviorRecipe
import core.thing.thing

val burnToAsh = BehaviorRecipe("Burn to Ash", mapOf("name" to "\$itemName"))

val burnable = thing("Burnable") {
    param("fireHealth" to 1, "itemName" to "Item")
    props {
        value("fireHealth", "\$fireHealth")
        tag("Flammable")
    }
    behavior(burnToAsh)
}