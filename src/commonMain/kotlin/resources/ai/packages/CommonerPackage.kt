package resources.ai.packages

import core.GameState
import core.ai.packages.AIPackageTemplateResource
import core.ai.packages.aiPackages
import core.ai.packages.setUseGoal
import use.interaction.nothing.NothingEvent

class CommonerPackage : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage("Commoner") {
            template("Creature")
            idea("Want Food") {
                cond { GameState.timeManager.getPercentDayComplete() in listOf(25, 50, 75) }
                act { owner ->
                    val target = (owner.inventory.getItems() + owner.location.getLocation().getItems(perceivedBy = owner)).firstOrNull { it.properties.tags.has("Food") }
                    target?.let {
                        owner.setUseGoal(target, "eat")
                    } ?: NothingEvent(owner)
                }
            }
        }
    }
}
