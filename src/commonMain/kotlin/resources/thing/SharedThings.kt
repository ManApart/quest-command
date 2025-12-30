package resources.thing

import core.ai.behavior.BehaviorRecipe
import core.properties.props
import core.thing.Thing
import core.thing.ThingBuilder
import core.thing.thing
import core.utility.Backer

val burnToAsh = BehaviorRecipe("Burn to Ash", mapOf("name" to $$"$itemName"))

val burnable: Backer<ThingBuilder> = Backer(::burnable)

private suspend fun burnable(): ThingBuilder {
    return thing("Burnable") {
        param("fireHealth" to 1, "itemName" to "Item")
        props {
            value("fireHealth", "\$fireHealth")
            tag("Flammable")
        }
        behavior(burnToAsh)
    }
}
