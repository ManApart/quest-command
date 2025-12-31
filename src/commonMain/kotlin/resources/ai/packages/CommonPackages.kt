package resources.ai.packages

import conversation.dsl.hasTag
import core.FactKind
import core.GameState
import core.HowToUse
import core.ai.knowledge.clearUseGoal
import core.ai.knowledge.discover
import core.ai.knowledge.setUseTarget
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
                act { s ->
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
                cond { s -> s.perceivedCreatures().any { !it.hasTag("Predator") } }
                act { s ->
                    s.perceivedCreatures().firstOrNull { !it.hasTag("Predator") }
                        ?.let { s.discover(it, FactKind.AGGRO_TARGET) }
                }
            }
        }
    }
}
