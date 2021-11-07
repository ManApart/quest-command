package resources.traveling.location.location

import traveling.location.network.NetworkResource
import traveling.location.network.networks

class KanbaraCountryside : NetworkResource {
    override val values = networks {
        network("Kanbara Countryside") {
            locationNode("Farmer's Hut") {
                connection("Farmer's Hut Interior", y = -500)
                connection("An Open Field", x = 100)
            }

            locationNode("Kanbara Road") {
                connection {
                    x(-10)
                    connectsTo("Kanbara Gate", "Kanbara")
                }
                connection("Farmer's Hut"){
                    x(1000)
                    y(800)
                }
                connection("Kentle"){
                    x(2000)
                    y(1600)
                }
                connection("Clovenwood"){
                    x(3000)
                    y(2500)
                }
                connection("Cadeya"){
                    x(5000)
                    y(4000)
                }
            }

            locationNode("An Open Field") {
                location("Field")
                connection("Apple Tree", y = 100)
                connection("Barren Patch", y = -100)
                connection("Training Circle", x = 100)
                connection("Windmill", x = 150, y = 100)
            }

            locationNode("Windmill") {
                connection("Stairs") {
                    thing("Stairs")
                    part("Stairs")
                    z(20)
                    restricted(true)
                    connectsTo("Windmill - Second Floor", "Kanbara Countryside")
                }
            }

            locationNode("Windmill - Second Floor") {
                connection("Stairs") {
                    thing("Stairs")
                    part("Stairs")
                    z(20)
                    restricted(true)
                    connectsTo("Windmill - Third Floor", "Kanbara Countryside")
                }
            }

            locationNode("Apple Tree") {
                connection {
                    thing("Apple Tree")
                    part("Branches")
                    z(15)
                    restricted(true)
                    connectsTo("Apple Tree Branches", "Kanbara Countryside")
                }
            }

            locationNode("Apple Tree Branches")

            locationNode("Barren Patch") {
                connection("Cave Entrance", y = -100)
            }
            locationNode("Cave Entrance") {
                connection("Cave Mouth")
            }
            locationNode("Cave Mouth") {
                connection("Wall Crack") {
                    thing("Wall Crack")
                    restricted(true)
                    hidden(true)
                    connectsTo("Cave Tunnel", "Kanbara Countryside")
                }
            }
            locationNode("Cave Tunnel")
        }
    }
}