package resources.traveling.location.location

import traveling.location.location.LocationResource
import traveling.location.location.locations

class CommonLocations : LocationResource {
    override val values = locations {
        location("World")

        location("Inside") {
            props { tag("Inside") }
            weather("Inside Weather")
        }

        location("Outside") {
            props { tag("Outside") }
            weather("Outside Weather")
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
            extends("Inside")
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
            descriptionPointer("Field")
            activator("Wheat Field", x = 10)
        }

        location("Apple Tree") {
            extends("Outside")
            descriptionPointer("The tree's leaves rustle in the wind, dusting it with the smell of apples.")
            activator("Apple Tree")
            item("Apple"){
                location("high in the branches")
                z(15)
            }
            item("Apple"){
                location("at the base of the tree")
            }
            item("Dulled Hatchet"){
                location("leaning against the tree")
            }
        }

        location("Apple Tree Branches") {
            extends("Outside")
            descriptionPointer("The crisp smell of apples permeates the air.")
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
            extends("Inside")
            activator("Grain Bin")
            activator("Stairs")
            item("Pot")
        }

        location("Windmill - Second Floor") {
            extends("Inside")
            activator("Stairs")
        }

        location("Windmill - Third Floor") {
            extends("Inside")
            activator("Grain Chute")
        }
    }
}