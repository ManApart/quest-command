package resources.thing.activators

import core.properties.CONTAINER
import core.properties.OPEN
import core.properties.SIZE
import core.thing.activator.dsl.ActivatorResource
import core.thing.things
import resources.thing.burnToAsh
import resources.thing.burnable
import traveling.scope.LIGHT

class CommonActivators : ActivatorResource {

    override val values = things {
        thing("Apple Tree") {
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
            behavior("Climbable")
        }

        thing("Apple Tree Branches") {
            extends(burnable)
            description("The branches are too thin to sit on comfortably, but their leaves rustle contentedly.")
            param("fireHealth" to 5, "itemName" to "Apple Tree Branches")
            props {
                tag("Wood")
            }
        }

        thing("Wheat Field") {
            extends(burnable)
            description("The golden shafts of wheat whisper as they brush against each other.")
            sound(5, "a faint rustling sound")
            param("fireHealth" to 2, "itemName" to "Wheat Field")
            behavior(
                "Slash Harvest",
                "itemName" to "Wheat Bundle",
                "message" to "The wheat falls with little more than a whisper.",
                "count" to 3
            )
        }

        thing("Logs") {
            extends(burnable)
            description("A pile of logs.")
            param("fireHealth" to 5, "itemName" to "Ash")
            props {
                tag("Wood")
            }
        }

        thing("Well") {
            description("The well's smoothed stones seem at once immovable and soft, as if they are a natural part of the terrain.")
            props {
                tag("Water Source")
            }
        }

        thing("Kanbara Gate (Open)") {
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

        thing("Kanbara Gate (Closed)") {
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

        thing("Range") {
            description("The old metal is pitted, but its belly glows a contented orange.")
            props {
                value("fireHealth", 2)
                tag("Metal", "Flammable", "Range")
            }
            behavior("Burn Out", "fireHealth" to 5)
        }

        thing("Grain Chute") {
            description("Placing grain in this chute will mill it. The milled grain can be picked up from the grain bin below.")
            body("Grain Chute")
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

        thing("Grain Bin") {
            description("Place grain in the chute above to pick up the milled contents here.")
            body("Grain Bin")
            props {
                value("size", 3)
                tag("Open", "Container")
            }
        }

        thing("Stairs") {
            description("The stairs lead to another floor.")
            body("Stairs")
            props {
                tag("Climbable")
            }
            behavior("Climbable")
        }

        thing("City Wall") {
            description("The squared stones rise high above you.")
            body("City Wall")
            props {
                tag("Climbable")
            }
            behavior("Climbable")
        }

        thing("Wall Crack") {
            description("This crack is large enough to squeeze through.")
            body("Wall Crack")
        }

        thing("Wall Scone") {
            description("The torch is wrought with iron against the wall.")
            props {
                value(LIGHT, 7)
            }
        }

        thing("Forge") {
            description("The large plates of iron seethe and glow.")
            props {
                value("fireHealth", 20)
                tag("Metal", "Flammable", "Forge")
            }
        }

        thing("Smithing Supply Chest") {
            description("The over-thick iron banding is a giveaway that this is usually used for smithing supplies.")
            body("Medium Container")
//            item("Iron Ingot", "Leather")
            props {
                value(SIZE to 10)
                tag(CONTAINER, OPEN)
            }
        }

    }

}