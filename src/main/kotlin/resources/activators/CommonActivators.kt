package resources.activators

import core.ai.behavior.BehaviorRecipe
import core.target.activator.dsl.ActivatorResource
import core.target.targets

class CommonActivators : ActivatorResource {
    private val burnToAsh = BehaviorRecipe("Burn to Ash", mapOf("name" to "\$itemName"))

    override val values = targets {
        target("Burnable") {
            param("fireHealth" to 1, "itemName" to "Item")
            props {
                value("fireHealth", "\$fireHealth")
                tag("Flammable")
            }
            behavior(burnToAsh)
        }

        target("Apple Tree") {
            description("The apple tree's gnarled branches provide good footholds, and sparks of ruby dance among the emerald leaves.")
            body("tree")
            props {
                value("chopHealth", 5)
                value("fireHealth", 5)
                tag("Climbable", "Container", "Flammable", "Open", "Wood")
            }
            item("Apple")
            behavior("Chop Tree", "treeName" to "Apple Tree", "resultItemName" to "Apple")
            behavior(burnToAsh)
            behavior("Climbable", "climbable" to "Apple Tree")
        }

        target("Apple Tree Branches") {
            extends("Burnable")
            description("The branches are too thin to sit on comfortably, but their leaves rustle contentedly.")
            param("fireHealth" to 5, "itemName" to "Apple Tree Branches")
            props {
                tag("Wood")
            }
        }


    }

}