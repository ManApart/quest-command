package resources.ai.desire

import core.ai.desire.DesireResource
import core.ai.desire.desires

class CommonDesires : DesireResource {
    override val values = desires {
        agenda("Nothing")

        context("creatures") { source, _ -> source.location.getLocation().getCreatures(perceivedBy = source).filter { it != source } }

        cond({ source, _ -> source.properties.tags.has("Predator") }) {
            //Eventually use factions + actions to create how much something likes something else
            context("target") { source, c -> c.things("creatures", source)?.firstOrNull { !it.properties.tags.has("Predator") } }
            cond({ s, c -> c.thing("target", s) != null }) {
                agenda("FindAndAttack")
            }
        }
    }
}