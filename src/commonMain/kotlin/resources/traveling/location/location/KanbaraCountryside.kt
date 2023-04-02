package resources.traveling.location.location

import traveling.location.network.NetworkResource
import traveling.location.network.networks

class KanbaraCountryside : NetworkResource {
    override val values = networks {
        network("Kanbara Countryside") {
            locationNode("Kanbara Road") {
                connection {
                    x(-10)
                    connectsTo("Kanbara Gate", "Kanbara")
                }
                connection("Northern Woods", y = 10)
                connection("Southern Woods", y = -10)
                connection("Kentle", x = 1000, y = 800)
                connection("Clovenwood", x = 2000, y = 1600)
                connection("Cadeya", x = 3000, y = 2500)
            }
            locationNode("Kentle") {
                connection("Farmer's Hut", y = -500)
                connection("Windmill", x = 250, y = -400)
                connection("Smith's Hut", x = -500)
            }

            locationNode("Farmer's Hut") {
                location("Farmer's Hut") {
                    creature("Farmer") {
                        learnBedLocation("Simple Bed", "Farmer's Hut Interior", "Kanbara Countryside")
                        learnWorkLocation("An Open Field", "Kanbara Countryside")
                    }
                }
            }
            locationNode("Farmer's Hut Interior") {
                connection("Farmer's Hut", y = 500)
            }

            locationNode("An Open Field") {
                location("Field")
                connection("Apple Tree", y = 100)
                connection("Barren Patch", y = -100)
                connection("Training Circle", x = 100)
                connection("Farmer's Hut", x = -100)
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
                connection("Cave Entrance", x = -100)
            }

            locationNode("Apple Tree Branches")

            locationNode("Barren Patch")

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

            locationNode("Dwarven Tear River East") {
                connection {
                    x(10)
                    y(-10)
                    connectsTo("Kanbara City", "Kanbara")
                }
            }
            locationNode("Dwarven Tear River West") {
                connection {
                    x(-10)
                    y(10)
                    connectsTo("Kanbara City", "Kanbara")
                }
                connection("Dwarven Tear River East", x = -10)
                connection("Dwarven Tear River North Fork", x = 100, y = -200)
                connection("Dwarven Tear River South Fork", x = 100, y = -200)
            }
            locationNode("Dwarven Tear River North Fork") {
                connection("Eldar Island", y = -100)
            }
            locationNode("Dwarven Tear River South Fork") {
                connection("Eldar Island", y = 100)
            }
            locationNode("Eldar Island")
            locationNode("Dwarven Tear River Mouth") {
                connection("Dwarven Tear River North Fork", x = -100, y = 100)
                connection("Dwarven Tear River South Fork", x = -100, y = 100)
            }
        }
    }
}