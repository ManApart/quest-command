package resources.traveling.location.location

import traveling.location.network.NetworkStrings.KANBARA
import traveling.location.network.NetworkResource
import traveling.location.network.networks

class KanbaraCity : NetworkResource {
    override val values = networks {
        network(KANBARA) {
            locationNode("Kanbara Gate") {
                connection("Kanbara City") {
                    restricted(true)
                    x(-100)
                }
            }

            locationNode("Kanbara City") {
                connection("Kanbara Pub")
                connection("Kanbara Manor", x = -10, y = 10)
                connection("Kanbara City South") {
                    origin(-10, -20)
                }
            }

            locationNode("Kanbara City South") {
                location("Kanbara City")
            }

            locationNode("Kanbara Wall North") {
                location("City Wall")
                connection("Kanbara Gate", x = 100, y = -100)
                connection {
                    thing("City Wall")
                    climbing()
                    connectsTo("Kanbara City", KANBARA, "City Wall")
                    restricted(true)
                    y(-100)
                }
            }

            locationNode("Kanbara Wall South") {
                location("City Wall")
                connection("Kanbara Gate", x = 100, y = 100)
                connection {
                    thing("City Wall")
                    climbing()
                    connectsTo("Kanbara City South", KANBARA, "City Wall")
                    restricted(true)
                    y(-100)
                }
            }
        }
    }
}
