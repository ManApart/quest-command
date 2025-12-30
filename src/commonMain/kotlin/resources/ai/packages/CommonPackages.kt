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

        aiPackage("Predator") {
            template("Creature")
            idea("Rest") {
                cond { !GameState.timeManager.isNight() }
                act { RestEvent(it, 2) }
            }

        }

    }

}
