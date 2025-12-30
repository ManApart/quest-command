package resources.ai.packages

import core.GameState
import core.ai.packages.*
import status.rest.RestEvent
import status.stat.STAMINA
import traveling.move.startMoveEvent
import use.eat.EatFoodEvent
import use.interaction.nothing.NothingEvent

class CommonPackages : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage("Predator") {
            template("Creature")
            idea("Rest") {
                cond { !GameState.timeManager.isNight() }
                act { RestEvent(it, 2) }
            }
        }
    }
}
