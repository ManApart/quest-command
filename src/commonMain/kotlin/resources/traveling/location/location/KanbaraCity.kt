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
                    restricted(true)
                    origin(-10, -20)
                }
                connection {
                    thing("City Wall")
                    climbing()
                    connectsTo("Kanbara City Wall Top North", KANBARA, "City Wall")
                    restricted(true)
                    y(100)
                }
            }

            locationNode("Kanbara City South") {
                location("Kanbara City")
                connection {
                    thing("City Wall")
                    climbing()
                    connectsTo("Kanbara City Wall Top South", KANBARA, "City Wall")
                    restricted(true)
                    y(-100)
                }
            }

            locationNode("Kanbara Wall Top North") {
                location("City Wall")
            }

            locationNode("Kanbara Wall Top South") {
                location("City Wall")
            }

            locationNode("Kanbara Wall North") {
                location("City Wall")
                connection("Kanbara Gate", x = 100, y = -100)
                connection {
                    thing("City Wall")
                    climbing()
                    connectsTo("Kanbara City Wall Top North", KANBARA, "City Wall")
                    restricted(true)
                    y(100)
                }
            }

            locationNode("Kanbara Wall South") {
                location("City Wall")
                connection("Kanbara Gate", x = 100, y = 100)
                connection {
                    thing("City Wall")
                    climbing()
                    //TODO - if you can climb up, should be able to jump down something
                    connectsTo("Kanbara City Wall Top South", KANBARA, "City Wall")
                    restricted(true)
                    y(-100)
                }
            }
        }
    }
}
