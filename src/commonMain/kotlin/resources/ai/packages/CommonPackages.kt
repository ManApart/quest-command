package resources.ai.packages

import core.GameState
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

            idea("Scratch Tree") {
                //TODO
            }
            idea("Hunt") {
                //TODO
            }
        }
    }
}
