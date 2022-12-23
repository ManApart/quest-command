package resources.ai.desire

import core.GameState
import core.ai.desire.DesireResource
import core.ai.desire.desires
import core.thing.Thing
import status.stat.STAMINA
import time.TimeManager

class CommonDesires : DesireResource {
    override val values = desires {
        agenda("Nothing")

        cond({ source -> source.mind.getAggroTarget() != null }) {
            priority = 70
            agenda("Attack")
        }

        cond({ source -> source.properties.tags.has("Predator") }) {
            agenda("Wander")

            cond({ _ -> !GameState.timeManager.isNight() }) {
                agenda("Sleep On Ground")
            }

            cond({ s -> s.soul.getCurrent(STAMINA) < s.soul.getTotal(STAMINA) / 10 }) {
                agenda("Sleep On Ground")
            }

            cond({ s -> s.items().firstOrNull { it.properties.tags.has("Food") } != null }) {
                agenda("Eat Food on Ground")
            }

            cond(20, { s -> s.activators().firstOrNull { it.name.contains("Tree") } != null }) {
                agenda("Scratch Tree")
            }
            //Eventually use factions + actions to create how much something likes something else
            cond({ s -> s.creatures().firstOrNull { !it.properties.tags.has("Predator") } != null }) {
                additionalPriority = 10
                agenda("Hunt")
            }
        }
    }
}

private fun Thing.creatures(): List<Thing> {
    return location.getLocation().getCreatures(perceivedBy = this).filter { it != this }
}

private fun Thing.activators(): List<Thing> {
    return location.getLocation().getActivators(perceivedBy = this).filter { it != this }
}

private fun Thing.items(): List<Thing> {
    return location.getLocation().getItems(perceivedBy = this).filter { it != this }
}