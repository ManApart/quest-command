package resources.traveling.location.location

import traveling.location.location.LocationResource
import traveling.location.location.locations

class KanbaraCountrysideLocations : LocationResource {
    override val values = locations {
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

        location("Kanbara Gate") {
            extends("Outside")
            description("The large gate of Kanbara seems to be its only entrance point.")
            activator("Kanbara Gate (Closed)")
            props {
                tag("City")
            }
        }
    }
}