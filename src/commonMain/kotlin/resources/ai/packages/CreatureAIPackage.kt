package resources.ai.packages

import conversation.dsl.hasTag
import core.GameState
import core.HowToUse
import core.TagKey
import core.ai.knowledge.clearUseGoal
import core.ai.knowledge.setUseTarget
import core.ai.packages.*
import core.utility.random
import status.rest.RestEvent
import status.stat.STAMINA
import traveling.move.startMoveEvent
import traveling.travel.TravelStartEvent
import use.eat.EatFoodEvent
import use.interaction.InteractEvent

class CreatureAIPackage : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage("Creature") {

            idea("Rest") {
                cond { s -> s.soul.getCurrent(STAMINA) < s.soul.getTotal(STAMINA) / 10 }
                act { RestEvent(it, 2) }
            }

            idea("Attack", 70) {
                cond { it.mind.getAggroTarget() != null && it.canReach(it.mind.getAggroTarget()!!.position) }
                act {
                    clawAttack(it.mind.getAggroTarget()!!, it)
                }
            }

            idea("Start Travel") {
                cond { s ->
                    (s.mind.getAggroTarget() ?: s.mind.getUseTargetThing())
                        ?.let { s.location != it.location } ?: false
                }
                act { s ->
                    val goal = (s.mind.getAggroTarget() ?: s.mind.getUseTargetThing())?.location ?: return@act null
                    plotRouteAndStartTravel(s, goal)
                }
            }

            idea("Travel", 60) {
                cond { s -> s.mind.route != null }
                act { s ->
                    s.mind.route?.getNextStep(s.location)?.destination?.location
                        ?.let { TravelStartEvent(s, destination = it) }
                }
            }

            idea("Move to Use Target", 50) {
                cond { s -> s.mind.getUseTargetThing()?.let { !s.canReach(it.position) } ?: false }
                act { startMoveEvent(it, destination = it.mind.getUseTargetThing()!!.position) }
            }

            idea("Move to Aggro Target", 70) {
                cond { it.hasAggroTarget() && !it.canReach(it.mind.getAggroTarget()!!.position) }
                act { startMoveEvent(it, destination = it.mind.getAggroTarget()!!.position) }
            }

            //TODO - Maybe last eaten + amount of time
            //TODO - second version to go look for food
            idea("Want Food", takesTurn = false) {
                cond { s ->
                    GameState.timeManager.getPercentDayComplete() in listOf(25, 50, 75) &&
                            s.perceivedItemsAndInventory().firstOrNull { it.hasTag(TagKey.FOOD) } != null
                }
                act { s ->
                    s.perceivedItemsAndInventory().firstOrNull { it.hasTag(TagKey.FOOD) }
                        ?.let { s.setUseTarget(it, HowToUse.EAT) }
                }
            }

            idea("Eat Food", 45) {
                cond { it.canReachGoal(HowToUse.EAT) }
                actions { s ->
                    listOfNotNull(
                        s.mind.getUseTargetThing()?.let { EatFoodEvent(s, it) },
                        s.clearUseGoal()
                    )
                }
            }

            idea("Interact", 40) {
                cond { it.canReachGoal(HowToUse.INTERACT) }
                actions { s ->
                    listOfNotNull(
                        s.mind.getUseTargetThing()?.let { InteractEvent(s, it) },
                        s.clearUseGoal()
                    )
                }
            }

            idea("Wander", 10) {
                cond { s -> s.location.getLocation().getThings(s).filter { it != s }.isNotEmpty() }
                act {
                    it.location.getLocation().getThings(it).random()?.position?.let { pos ->
                        startMoveEvent(it, destination = pos)
                    }
                }
            }
        }
    }
}
