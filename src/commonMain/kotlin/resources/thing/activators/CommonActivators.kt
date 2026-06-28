package resources.thing.activators

import core.properties.ParameterStrings.FIRE_HEALTH
import core.properties.ParameterStrings.ITEM_NAME
import core.properties.TagStrings
import core.properties.TagStrings.CONTAINER
import core.properties.TagStrings.OPEN
import core.properties.TagStrings.SIZE
import core.properties.TagStrings.WOOD
import core.properties.ValueStrings
import core.thing.activator.dsl.ActivatorResource
import core.thing.things
import crafting.material.MaterialStrings.IRON
import crafting.material.MaterialStrings.PLANT
import crafting.material.MaterialStrings.STONE
import resources.thing.burnToAsh
import resources.thing.burnable
import traveling.scope.LIGHT

class CommonActivators : ActivatorResource {

    override suspend fun values() = things {
        thing("Apple Tree") {
            material(WOOD)
            description("The apple tree's gnarled branches provide good footholds, and sparks of ruby dance among the emerald leaves.")
            body("tree")
            props {
                value("chopHealth", 5)
                value(ValueStrings.FIRE_HEALTH, 5)
                tag("Climbable", "Container", "Flammable", "Open", "Wood")
            }
            item("Apple")
            behavior("Chop Tree", "treeName" to "Apple Tree", "resultItemName" to "Apple")
            behavior(burnToAsh)
            behavior("Climbable")
        }

        thing("Apple Tree Branches") {
            material(WOOD)
            extends(burnable.get())
            description("The branches are too thin to sit on comfortably, but their leaves rustle contentedly.")
            param(FIRE_HEALTH to 5, ITEM_NAME to "Apple Tree Branches")
            props {
                tag(WOOD)
            }
        }

        thing("Wheat Field") {
            extends(burnable.get())
            material(PLANT)
            description("The golden shafts of wheat whisper as they brush against each other.")
            sound(5, "a faint rustling sound")
            param(FIRE_HEALTH to 2, ITEM_NAME to "Wheat Field")
            behavior("Tend Crop")
            behavior(
                "Slash Harvest",
                ITEM_NAME to "Wheat Bundle",
                "message" to "The wheat falls with little more than a whisper.",
                "count" to 3
            )
            props {
                tag(TagStrings.FARMABLE)
            }
        }

        thing("Logs") {
            extends(burnable.get())
            material(WOOD)
            description("A pile of logs.")
            param(FIRE_HEALTH to 5, ITEM_NAME to "Ash")
            props {
                tag(WOOD)
            }
        }

        thing("Well") {
            material(STONE)
            description("The well's smoothed stones seem at once immovable and soft, as if they are a natural part of the terrain.")
            props {
                tag("Water Source")
            }
        }

        thing("Kanbara Gate (Open)") {
            material(IRON)
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
            material(IRON)
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
            material(IRON)
            description("The old metal is pitted, but its belly glows a contented orange.")
            props {
                value("fireHealth", 2)
                tag("Metal", "Flammable", "Range")
            }
            behavior("Burn Out", "fireHealth" to 5)
        }

        thing("Grain Chute") {
            material(STONE)
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
            material(STONE)
            description("Place grain in the chute above to pick up the milled contents here.")
            body("Grain Bin")
            props {
                value("size", 3)
                tag("Open", "Container")
            }
        }

        thing("Stairs") {
            material(WOOD)
            description("The stairs lead to another floor.")
            body("Stairs")
            props {
                tag("Climbable")
            }
            behavior("Climbable")
        }

        thing("City Wall") {
            material(STONE)
            description("The squared stones rise high above you.")
            body("City Wall")
            props {
                tag("Climbable")
            }
            behavior("Climbable")
        }

        thing("Wall Crack") {
            material(STONE)
            description("This crack is large enough to squeeze through.")
            body("Wall Crack")
        }

        thing("Wall Scone") {
            material(IRON)
            description("The torch is wrought with iron against the wall.")
            props {
                value(LIGHT, 7)
            }
        }

        thing("Forge") {
            material(IRON)
            description("The large plates of iron seethe and glow.")
            props {
                value("fireHealth", 20)
                tag("Metal", "Flammable", "Forge")
            }
        }

        thing("Smithing Supply Chest") {
            material(WOOD)
            description("The over-thick iron banding is a giveaway that this is usually used for smithing supplies.")
            body("Medium Container")
            item("Bronze Ingot", "Leather")
            props {
                value(SIZE to 10)
                tag(CONTAINER, OPEN)
            }
        }

        thing("Simple Bed") {
            material(WOOD)
            description("A simple, unadorned bed.")
            behavior("Rest")
            props {
                tag("Bed")
            }
        }

    }

}
