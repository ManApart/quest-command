package resources.traveling.location.location

import core.GameState
import core.utility.RandomManager
import traveling.location.location.LocationResource
import traveling.location.location.locations
import traveling.scope.LIGHT

class CommonLocations : LocationResource {
    override val values = locations {
        location("World")

        location("Inside") {
            props { tag("Inside") }
            weather {
                option("Distant Rain") { RandomManager.isSuccess(40) }
                option("Distant Storm") { RandomManager.isSuccess(10) }
                option("Calm")
            }
        }

        location("Inside Building") {
            extends("Inside")
            props {
                value(LIGHT, 5)
            }
        }

        location("Outside") {
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

        location("Kanbara") {
            extends("Outside")
            description("The bustling port town of Kanbara is one of the most densely packed cities in all of Lenovia.")
        }

        location("Kanbara City") {
            extends("Outside")
            description("The city of Kanbara is one of the most densely packed cities in all of Lenovia.")
            activator("City Wall")
            props {
                tag("City")
            }
        }

        location("City Wall") {
            extends("Outside")
            activator("City Wall")
            props {
                tag("City")
            }
        }

        location("Kanbara Gate") {
            extends("Outside")
            description("The large gate of Kanbara seems to be its only entrance point.")
            activator("Kanbara Gate (Closed)")
            props {
                tag("City")
            }
        }

        location("Farmer's Hut") {
            extends("Outside")
            description("The hut is small, but it's hewn logs are worn by the passage of time and polished by the passing of many caring hands.")
            activator("Well")
            creature("Farmer")
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

        location("Field") {
            extends("Outside")
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
            description("The crisp smell of apples permeates the air.")
            activator("Apple Tree Branches")
            item("Apple")
            item("Apple")
            item("Apple")
        }

        location("Barren Patch") {
            extends("Outside")
            creature("Rat")
        }

        location("Training Circle") {
            extends("Outside")
            description("The small sandy field is soft in its center; the pooled sand is churned from its often use. The edges of the circle are worn and nearly hard as rock.")
            creature("Magical Dummy")
        }

        location("Windmill") {
            extends("Inside Building")
            activator("Grain Bin")
            activator("Stairs")
            item("Pot")
        }

        location("Windmill - Second Floor") {
            extends("Inside Building")
            activator("Stairs")
        }

        location("Windmill - Third Floor") {
            extends("Inside Building")
            activator("Grain Chute")
        }

        location("Cave Entrance") {
            extends("Outside")
            item("Lantern")
        }

        location("Cave Mouth") {
            extends("Inside")
            activator("Wall Crack")
        }

        location("Cave Tunnel") {
            extends("Inside")
        }
    }
}