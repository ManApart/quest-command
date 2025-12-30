package resources.ai.packages

import core.ai.packages.AIPackageTemplateResource
import core.ai.packages.aiPackages
import core.ai.packages.canReachGoal
import core.ai.packages.clawAttack
import core.ai.packages.clearUseGoal
import core.ai.packages.hasUseTarget
import status.rest.RestEvent
import status.stat.STAMINA
import traveling.move.startMoveEvent
import use.eat.EatFoodEvent

class CreaturePackage : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage("Creature") {

            idea("Rest") {
                cond { s -> s.soul.getCurrent(STAMINA) < s.soul.getTotal(STAMINA) / 10 }
                act { RestEvent(it, 2) }
            }

            idea("Attack", 70) {
                cond { it.mind.getAggroTarget() != null }
                act {
                    clawAttack(it.mind.getAggroTarget()!!, it)
                }
            }

            //TODO - need move to location as well
            //If usetarget in different location, create map to get there
            //  map available, move to location
            idea("Move to Use Target", 50) {
                cond { it.hasUseTarget() && !it.canReach(it.mind.getUseTargetThing()!!.position) }
                act { startMoveEvent(it, destination = it.mind.getUseTargetThing()!!.position) }
            }

            idea("Eat Targeted Food", 50) {
                cond { it.canReachGoal("eat") }
                act {
                    EatFoodEvent(it, it.mind.getUseTargetThing()!!)
                    it.clearUseGoal()
                }
            }
            idea("Wander") {
                act {
                    val target = it.location.getLocation().getThings(it).random()
                    startMoveEvent(it, destination = target.position)
                }
            }
        }
    }
}
