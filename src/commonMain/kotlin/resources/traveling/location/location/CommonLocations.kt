package resources.traveling.location.location

import core.GameState
import core.utility.RandomManager
import traveling.location.location.LocationResource
import traveling.location.location.locations

class CommonLocations : LocationResource {
    override val values = locations {
        location("World") {
            material("Dirt")
        }

        location("Inside") {
            material("Dirt")
            props { tag("Inside") }
            weather {
                option("Distant Rain") { RandomManager.isSuccess(40) }
                option("Distant Storm") { RandomManager.isSuccess(10) }
                option("Calm")
            }
        }

        location("Inside Building") {
            extends("Inside")
            material("Wood")
            activator("Wall Scone", x = 10)
            activator("Wall Scone", x = -10)
            lightLevel(2)
        }

        location("Outside") {
            material("Dirt")
            props { tag("Outside") }
            weather {
                option("Windy") { RandomManager.isSuccess(20) }
                option("Strong Wind") { RandomManager.isSuccess(10) }
                option("Gale") { RandomManager.isSuccess(5) }
                option("Light Fog") { RandomManager.isSuccess(20) }
                option("Fog") { RandomManager.isSuccess(5) }
                option("Rain") { RandomManager.isSuccess(20) }
                option("Heavy Rain") { RandomManager.isSuccess(30) }
                option("Thunderstorm") { RandomManager.isSuccess(5) }
                option("Soft Wind")
            }
            lightLevel(8)
        }

        location("City Wall") {
            extends("Outside")
            material("Gravel")
            activator("City Wall")
            props {
                tag("City")
            }
        }

        location("Farmer's Hut") {
            extends("Outside")
            description("The hut is small, but it's hewn logs are worn by the passage of time and polished by the passing of many caring hands.")
            activator("Well")
            item("Bucket")
            props {
                tag("City")
            }
        }

        location("Farmer's Hut Interior") {
            extends("Inside Building")
            description("The thatched roof hangs close to the ground; the single room is empty but for a small cooking range.")
            activator("Range") {
                location("by the door")
                vector(y = 10)
            }
            item("Tinder Box") {
                location("on the range")
                vector(y = 10, z = 5)
            }
            item("Pie Tin") {
                location("on the range")
                vector(y = 10, z = 5)
            }
            item("Apple Pie Recipe") {
                location("on the range")
                vector(y = 10, z = 5)
            }
            props {
                tag("Home")
            }
        }

        location("Smith's Hut") {
            extends("Outside")
            description("The open air hut smells of warm earth, ash, and iron. The posts and roof are blackened and dry.")
            item("Tinder Box") {
                location("near the forge")
                vector(x = 4)
            }
            activator("Forge", x = -5)
            activator("Smithing Supply Chest", y = 5)
            props {
                tag("City")
            }
        }

        location("Field") {
            extends("Outside")
            material("Plant")
            description {
                option("The red sun fills the waving grasses, making them glow. They sway like licks of flame.") {
                    GameState.timeManager.getHour() in 20..30 || GameState.timeManager.getHour() in 70..80
                }
                option("You feel the waist high grasses brush against you. You can hear their rustle expand into the distance.") {
                    GameState.timeManager.isNight()
                }
                option("The waist high grasses stretch into the distance. They don't obscure your view, but as they drift in the wind they give you the sensation of floating on a calm sea.")
            }
            activator("Wheat Field", x = 10)
            sound(1, "the occasional chirping of a bird")
        }

        location("Apple Tree") {
            extends("Outside")
            material("Plant")
            description("The tree's leaves rustle in the wind, dusting it with the smell of apples.")
            sound(1, "the soft rustle of many leaves")
            activator("Apple Tree")
            item("Apple") {
                location("high in the branches")
                z(15)
            }
            item("Apple") {
                location("at the base of the tree")
            }
            item("Dulled Hatchet") {
                location("leaning against the tree")
            }
        }

        location("Apple Tree Branches") {
            extends("Outside")
            material("Bark")
            description("The crisp smell of apples permeates the air.")
            activator("Apple Tree Branches")
            item("Apple")
            item("Apple")
            item("Apple")
        }

        location("Barren Patch") {
            extends("Outside")
            description("The surrounding grasses are yellow and thin. Towards the center the ground is nothing more than packed dirt.")
            creature("Rat")
        }

        location("Training Circle") {
            extends("Outside")
            description("The small sandy field is soft in its center; the pooled sand is churned from its often use. The edges of the circle are worn and nearly hard as rock.")
            creature("Magical Dummy")
        }

        location("Windmill") {
            extends("Inside Building")
            material("Stone")
            description("Large hewed stone encircle encircle a patchwork floor. It smells of wheat and yeast.")
            activator("Grain Bin")
            activator("Stairs")
            item("Pot")
        }

        location("Windmill - Second Floor") {
            description("The light is made golden by dust. The air is heavy.")
            extends("Inside Building")
            activator("Stairs")
        }

        location("Windmill - Third Floor") {
            description("A small set of windows gives a view of the outside. The stone was long ago plastered over and is now yellow brown.")
            extends("Inside Building")
            activator("Grain Chute")
        }

        location("Cave Entrance") {
            extends("Outside")
            description("The large wall of rock and moss is rended by shadow.")
            item("Lantern")
        }

        location("Cave Mouth") {
            extends("Inside")
            material("Stone")
            description("The air is humid and heavy, interrupted by gasps of cooler wind from outside.")
            activator("Wall Crack")
        }

        location("Cave Tunnel") {
            extends("Inside")
            material("Stone")
            description("The damp, cool stone runs along either side of the tunnel.")
        }
    }
}