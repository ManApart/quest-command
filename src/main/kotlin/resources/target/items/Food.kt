package resources.target.items

import core.target.item.ItemResource
import core.target.targets
import resources.target.burnToAsh
import resources.target.burnable

class Food : ItemResource {

    override val values = targets {
        target(burnable)

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


    }

}