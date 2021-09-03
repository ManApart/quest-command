package resources.target

import core.ai.behavior.BehaviorRecipe
import core.target.target

val burnToAsh = BehaviorRecipe("Burn to Ash", mapOf("name" to "\$itemName"))

val burnable = target("Burnable") {
    param("fireHealth" to 1, "itemName" to "Item")
    props {
        value("fireHealth", "\$fireHealth")
        tag("Flammable")
    }
    behavior(burnToAsh)
}