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
            material("Gravel")
            description("The city of Kanbara is one of the most densely packed cities in all of Lenovia.")
            activator("City Wall")
            props {
                tag("City")
            }
        }

        location("Kanbara Gate") {
            extends("Outside")
            material("Gravel")
            description("The large gate of Kanbara seems to be its only entrance point.")
            activator("Kanbara Gate (Closed)")
            props {
                tag("City")
            }
        }

        location("Northern Woods") {
            extends("Outside")
            description("Trees stand like sentries, waiting for the future.")
        }

        location("Southern Woods") {
            extends("Outside")
            description("The ground is think with leaves.")
        }

        location("Kentle") {
            extends("Outside")
            description("The small woodland village smells of both forest and sawdust.")
        }

        location("Clovenwood") {
            extends("Outside")
            material("Plant")
            description("Mint and ginger float on the wind. The result of the unique plants that grow around and through the village lead to its name.")
        }

        location("Cadeya") {
            extends("Outside")
            description("Mint and ginger float on the wind. The unique plants that grow around and through the village prompted its name.")
        }

    }
}