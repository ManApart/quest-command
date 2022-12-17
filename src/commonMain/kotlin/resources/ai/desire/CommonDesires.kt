package resources.ai.desire

import core.ai.desire.DesireResource
import core.ai.desire.desires
import core.thing.Thing

class CommonDesires : DesireResource {
    override val values = desires {
        agenda("Nothing")

        cond({ source -> source.mind.getAggroTarget() != null }) {
            priority = 70
            agenda("Attack")
        }

        cond({ source -> source.properties.tags.has("Predator") }) {
            //Eventually use factions + actions to create how much something likes something else
            cond({ s -> s.creatures().firstOrNull { !it.properties.tags.has("Predator") } != null }) {
                agenda("FindAndAttack")
            }
        }
    }
}

private fun Thing.creatures(): List<Thing> {
    return location.getLocation().getCreatures(perceivedBy = this).filter { it != this }
}