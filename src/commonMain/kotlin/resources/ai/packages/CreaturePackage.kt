package resources.ai.packages

import conversation.dsl.hasTag
import core.GameState
import core.ai.knowledge.HowToUse
import core.ai.knowledge.clearUseGoal
import core.ai.knowledge.setUseTarget
import core.ai.packages.*
import core.properties.TagKey
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

            //TODO - Maybe last eaten + amount of time
            idea("Want Food") {
                cond { s ->
                    GameState.timeManager.getPercentDayComplete() in listOf(25, 50, 75) &&
                            s.perceivedItemsAndInventory().firstOrNull { it.hasTag(TagKey.FOOD) } != null
                }
                actOrNot { s ->
                    s.perceivedItemsAndInventory().firstOrNull { it.hasTag(TagKey.FOOD.name) }
                        ?.let { s.setUseTarget(it, HowToUse.EAT) }
                }
            }

            idea("Eat Targeted Food", 45) {
                cond { it.canReachGoal(HowToUse.EAT) }
                actions { s ->
                    listOfNotNull(s.mind.getUseTargetThing()?.let { EatFoodEvent(s, it) },
                    s.clearUseGoal())
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
