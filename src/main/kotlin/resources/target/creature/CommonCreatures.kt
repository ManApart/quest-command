package resources.target.creature

import core.target.creature.CreatureResource
import core.target.targets

class CommonCreatures : CreatureResource {

    override val values = targets {
        target("Rat") {
            description("Mangy and red eyed.")
            ai("Cowardly Predator")
            body("Rat")
            soul("Health", 3)
            soul("Strength", 1)
            soul("Bare Handed", 2)
            props {
                tag("Small", "Predator")
            }
            item("Poor Quality Meat")
        }

        target("Farmer") {
            description("Salt of the earth; he's good people.")
            ai("Commoner")
            body("Human")
            soul("Health", 10)
            soul("Strength", 3)
            soul("Bare Handed", 2)
            props {
                tag("Human", "Commoner")
            }
        }

        target("Magical Dummy") {
            description("It's jerky movement is deeply unsettling.")
            ai("Player Controlled")
            body("Human")
            soul("Health", 10)
            soul("Stamina", 100)
            soul("Focus", 100)
            soul("Strength", 1)
            soul("Bare Handed", 2)
        }

    }

}