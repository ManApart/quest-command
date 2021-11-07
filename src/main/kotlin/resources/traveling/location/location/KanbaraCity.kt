package resources.traveling.location.location

import traveling.location.network.NetworkResource
import traveling.location.network.networks

class KanbaraCity : NetworkResource {
    override val values = networks {
        network("Kanbara") {
            locationNode("Kanbara Gate") {
                connection("Kanbara Wall South", x = -100, y = -100)
                connection("Kanbara Wall North", x = -100, y = 100)
                connection("Kanbara City") {
                    restricted(true)
                    x(-100)
                }
            }

            locationNode("Kanbara City") {
                connection("Kanbara Pub")
                connection("Kanbara Manor", x = -10, y = 10)
                connection("Kanbara City South") {
                    restricted(true)
                    vector(-10, -20)
                }
            }

            locationNode("Kanbara City South") {
                location("Kanbara City")
            }

            locationNode("Kanbara Wall North") {
                location("City Wall")
                connection {
                    thing("City Wall")
                    part("Wall Top")
                    connectsTo("Kanbara City", "Kanbara", "City Wall", "Wall Top")
                    restricted(true)
                    y(100)
                }
            }

            locationNode("Kanbara Wall South") {
                location("City Wall")
                connection {
                    thing("City Wall")
                    part("Wall Top")
                    connectsTo("Kanbara City South", "Kanbara", "City Wall", "Wall Top")
                    restricted(true)
                    y(-100)
                }
            }
        }
    }
}