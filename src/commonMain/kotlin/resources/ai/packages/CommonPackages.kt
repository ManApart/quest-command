package resources.ai.packages

import core.GameState
import core.ai.knowledge.HowToUse
import core.ai.knowledge.clearUseGoal
import core.ai.knowledge.setUseTarget
import core.ai.packages.AIPackageTemplateResource
import core.ai.packages.aiPackages
import core.ai.packages.canReachGoal
import core.ai.packages.clawAttack
import core.ai.packages.perceivedActivators
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
                    s.perceivedActivators().firstOrNull { it.name.contains("Tree") }?.let { s.setUseTarget(it, HowToUse.ATTACK) }
                }
            }

            idea("Attack Goal") {
                cond { it.canReachGoal(HowToUse.ATTACK) }
                actions { s ->
                    listOfNotNull(
                        s.mind.getUseTargetThing()?.let { clawAttack(it, s) },
                        s.clearUseGoal()
                    )
                }
            }
            idea("Hunt") {
                //TODO
            }
        }
    }
}
