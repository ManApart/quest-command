package resources.thing.creature

import core.FactKindStrings.WORK_TAGS
import core.properties.TagStrings.COMMONER
import core.properties.TagStrings.FARMABLE
import core.properties.TagStrings.PREDATOR
import core.properties.TagStrings.SMALL
import core.properties.ValueStrings.CAPACITY
import core.thing.creature.CreatureResource
import core.thing.things
import status.stat.AttributeStrings.AGILITY
import status.stat.AttributeStrings.FOCUS
import status.stat.AttributeStrings.HEALTH
import status.stat.AttributeStrings.STAMINA
import status.stat.AttributeStrings.STRENGTH

class CommonCreatures : CreatureResource {

    override suspend fun values() = things {
        thing("Rat") {
            description("Mangy and red eyed.")
            body("Rat")
            soul(HEALTH, 3)
            soul(STRENGTH, 1)
            soul("Bare Handed", 2)
            soul(AGILITY, 1)
            props {
                value(CAPACITY, 1)
                tag(SMALL, PREDATOR)
            }
            //TODO - make this a 'death item' that's spawned on death
            item("Poor Quality Meat")
        }

        thing("Farmer") {
            description("Salt of the earth; he's good people.")
            body("Human")
            soul(HEALTH, 10)
            soul(STRENGTH, 3)
            soul("Bare Handed", 2)
            soul(AGILITY, 1)
            mind {
                learn(WORK_TAGS, listOf(FARMABLE))
            }
            props {
                tag(COMMONER)
                value("Race", "Human")
                value(CAPACITY, 10)
            }
            item("Brown Pants", "Old Shirt")
        }

        thing("Magical Dummy") {
            description("It's jerky movement is deeply unsettling.")
            playerAI()
            body("Human")
            soul(HEALTH, 10)
            soul(STAMINA, 100)
            soul(FOCUS, 100)
            soul(STRENGTH, 1)
            soul("Bare Handed", 2)
            props {
                value(CAPACITY, 10)
            }
        }

    }

}
