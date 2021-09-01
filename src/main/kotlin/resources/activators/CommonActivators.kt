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

        target("Wheat Field") {
            extends("Burnable")
            description("The golden shafts of wheat whisper as they brush against each other.")
            param("fireHealth" to 2, "itemName" to "Wheat Field")
            behavior(
                "Slash Harvest",
                "itemName" to "Wheat Bundle",
                "message" to "The wheat falls with little more than a whisper.",
                "count" to 3
            )
        }

        target("Logs") {
            extends("Burnable")
            description("A pile of logs.")
            param("fireHealth" to 5, "itemName" to "Ash")
            props {
                tag("Wood")
            }
        }

        target("Well") {
            description("The well's smoothed stones seem at once immovable and soft, as if they are a natural part of the terrain.")
            props {
                tag("Water Source")
            }
        }

        target("Kanbara Gate (Open)") {
            description("The gate is large but not immovable.")
            props {
                tag("Door")
            }
            behavior(
                "Restrict Destination",
                "sourceNetwork" to "Kanbara",
                "sourceLocation" to "Kanbara Gate",
                "destinationNetwork" to "Kanbara",
                "destinationLocation" to "Kanbara City",
                "makeRestricted" to true,
                "message" to "The gates swing closed.",
                "replacementActivator" to "Kanbara Gate (Closed)"
            )
        }

        target("Kanbara Gate (Closed)") {
            description("The gate is large but not immovable.")
            props {
                tag("Door")
            }
            behavior(
                "Restrict Destination",
                "sourceNetwork" to "Kanbara",
                "sourceLocation" to "Kanbara Gate",
                "destinationNetwork" to "Kanbara",
                "destinationLocation" to "Kanbara City",
                "makeRestricted" to false,
                "message" to "The gates swing closed.",
                "replacementActivator" to "Kanbara Gate (Open)"
            )
        }

        target("Range") {
            description("The old metal is pitted, but its belly glows a contented orange.")
            props {
                value("fireHealth", 2)
                tag("Metal", "Flammable", "Range")
            }
            behavior("Burn Out", "fireHealth" to 5)
        }

        target("Grain Chute") {
            description("Placing grain in this chute will mill it. The milled grain can be picked up from the grain bin below.")
            body("Grain Chute") {
                part {
                    props {
                        value("size", 3)
                        tag("Container")
                    }
                }
            }
            props {
                value("size", 3)
                tag("Open", "Container")
            }
            behavior(
                "Mill",
                "sourceItem" to "Wheat Bundle",
                "resultItem" to "Wheat Flour",
                "resultItemNetwork" to "Kanbara Countryside",
                "resultItemLocation" to "Windmill"
            )
        }

        target("Grain Bin"){
            description("Place grain in the chute above to pick up the milled contents here.")
            props {
                value("size", 3)
                tag("Open", "Container")
            }
        }

        target("Stairs"){
            description("The stairs lead to another floor.")
            body("Stairs")
            behavior("Climbable", "climbable" to "Stairs")
        }

        target("City Wall"){
            description("The squared stones rise high above you.")
            body("City Wall")
            props {
                tag("Climbable")
            }
            behavior("Climbable", "climbable" to "City Wall")
        }

    }

}