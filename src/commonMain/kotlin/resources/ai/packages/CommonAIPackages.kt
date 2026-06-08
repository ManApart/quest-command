package resources.ai.packages

import conversation.dsl.hasTag
import core.FactKindStrings
import core.GameState
import core.HowToUseStrings
import core.ai.knowledge.clearUseGoal
import core.ai.knowledge.discover
import core.ai.knowledge.setUseTarget
import core.ai.packages.*
import status.rest.RestEvent

class CommonAIPackages : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage("Predator") {
            template("Creature")

            idea("Attack Goal", 50) {
                cond { it.canReachGoal(HowToUseStrings.ATTACK) }
                actions { s ->
                    listOfNotNull(
                        s.mind.getUseTargetThing()?.let { clawAttack(it, s) },
                        s.clearUseGoal()
                    )
                }
            }
            idea("Hunt", 30) {
                cond { s -> s.perceivedCreatures().any { !it.hasTag("Predator") } }
                act { s ->
                    s.perceivedCreatures().firstOrNull { !it.hasTag("Predator") }
                        ?.let { s.discover(it, FactKindStrings.AGGRO_TARGET) }
                }
            }

            idea("Sleep Outside") {
                cond { !GameState.timeManager.isNight() }
                act { RestEvent(it, 2) }
            }

            idea("Find Tree to Scratch") {
                cond { s -> s.perceivedActivators().any { it.name.contains("Tree") } }
                act { s ->
                    s.perceivedActivators().firstOrNull { it.name.contains("Tree") }?.let { s.setUseTarget(it, HowToUseStrings.ATTACK) }
                }
            }

        }
    }
}
