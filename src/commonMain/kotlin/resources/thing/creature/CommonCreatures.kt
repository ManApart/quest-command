package resources.thing.creature

import core.TagKey.PREDATOR
import core.thing.creature.CreatureResource
import core.thing.things

class CommonCreatures : CreatureResource {

    override suspend fun values() = things {
        thing("Rat") {
            description("Mangy and red eyed.")
            body("Rat")
            soul("Health", 3)
            soul("Strength", 1)
            soul("Bare Handed", 2)
            props {
                tag("Small", PREDATOR)
            }
            //TODO - make this a 'death item' that's spawned on death
            item("Poor Quality Meat")
        }

        thing("Farmer") {
            description("Salt of the earth; he's good people.")
            body("Human")
            soul("Health", 10)
            soul("Strength", 3)
            soul("Bare Handed", 2)
            mind{
                learn("WorkTags", listOf("Farmable"))
            }
            props {
                tag("Commoner")
                value("Race", "Human")
            }
            item("Brown Pants", "Old Shirt")
        }

        thing("Magical Dummy") {
            description("It's jerky movement is deeply unsettling.")
            playerAI()
            body("Human")
            soul("Health", 10)
            soul("Stamina", 100)
            soul("Focus", 100)
            soul("Strength", 1)
            soul("Bare Handed", 2)
        }

    }

}
