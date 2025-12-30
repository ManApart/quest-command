package resources.ai.packages

import core.GameState
import core.ai.knowledge.FactKind
import core.ai.knowledge.discover
import core.ai.packages.*
import status.rest.RestEvent

class CommonPackages : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage("Predator") {
            template("Creature")
            idea("Rest") {
                cond { !GameState.timeManager.isNight() }
                act { RestEvent(it, 2) }
            }

            idea("Find Tree to Scratch") {
                cond { s -> s.perceivedActivators().any { it.name.contains("Tree") } }
                actOrNot { s ->
                    s.perceivedActivators().firstOrNull { it.name.contains("Tree") }?.let { s.discover(it, FactKind.USE_TARGET) }
                }
            }

            idea("Scratch Tree") {
                //TODO
            }
            idea("Hunt") {
                //TODO
            }
        }
    }
}
