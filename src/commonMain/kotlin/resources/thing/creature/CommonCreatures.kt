package resources.thing.creature

import core.FactKind.WORK_TAGS
import core.TagKey.COMMONER
import core.TagKey.FARMABLE
import core.TagKey.PREDATOR
import core.thing.creature.CreatureResource
import core.thing.things
import status.stat.Attributes.AGILITY

class CommonCreatures : CreatureResource {

    override suspend fun values() = things {
        thing("Rat") {
            description("Mangy and red eyed.")
            body("Rat")
            soul("Health", 3)
            soul("Strength", 1)
            soul("Bare Handed", 2)
            soul(AGILITY, 1)
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
            soul(AGILITY, 1)
            mind {
                learn(WORK_TAGS, listOf(FARMABLE))
            }
            props {
                tag(COMMONER)
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
