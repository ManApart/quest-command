package resources.ai.packages

import combat.DamageType
import combat.attack.AttackEvent
import combat.attack.startAttack
import core.GameState
import core.ai.knowledge.DiscoverFactEvent
import core.ai.knowledge.Fact
import core.ai.knowledge.Subject
import core.ai.packages.AIPackageTemplateResource
import core.ai.packages.aiPackages
import core.events.Event
import core.properties.Properties
import core.properties.Values
import core.thing.Thing
import core.utility.RandomManager
import status.rest.RestEvent
import status.stat.STAMINA
import traveling.move.startMoveEvent
import traveling.position.ThingAim
import use.eat.EatFoodEvent
import use.interaction.nothing.NothingEvent

class CommonPackages : AIPackageTemplateResource {
    override val values = aiPackages {
        aiPackage("creature") {

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
            idea("Move to Use Target", 50) {
                cond { it.hasUseTarget() && !it.canReach(it.mind.getUseTarget()!!.position) }
                act { startMoveEvent(it, destination = it.mind.getUseTarget()!!.position) }
            }

            idea("Eat Targeted Food", 50) {
                //TODO get fact itself, check properties to see if goal is eat
                cond {
                    val useTarget = it.mind.getUseTarget()
                    useTarget != null && it.canReach(useTarget.position)
                }
                act { EatFoodEvent(it, it.mind.getUseTarget()!!) }
            }
        }

        aiPackage("Commoner") {
            template("creature")
            idea("Want Food") {
                cond { GameState.timeManager.getPercentDayComplete() in listOf(25, 50, 75) }
                act { owner ->
                    val target = (owner.inventory.getItems() + owner.location.getLocation().getItems(perceivedBy = owner)).firstOrNull { it.properties.tags.has("Food") }
                    target?.let {
                        owner.setGoal(target, "eat")
                    } ?: NothingEvent(owner)
                }
            }
        }

    }
}

private suspend fun Thing.hasUseTarget() = mind.getUseTarget() != null

private fun Thing.setGoal(target: Thing, howToUse: String): DiscoverFactEvent{
    return DiscoverFactEvent(this, Fact(Subject(target), "useTarget", Properties(Values(mutableMapOf("goal" to howToUse)))))
}

//TODO - forget goal
private fun Thing.clearGoal(target: Thing, howToUse: String): List<Event>{
    return emptyList()
}

private suspend fun clawAttack(target: Thing, creature: Thing): AttackEvent {
    val enemyBody = target.body
    val possibleParts = listOf(
        enemyBody.getPart("Right Foot"),
        enemyBody.getPart("Left Foot")
    )
    val thingPart = listOf(RandomManager.getRandom(possibleParts))
    val partToAttackWith = if (creature.body.hasPart("Small Claws")) {
        creature.body.getPart("Small Claws")
    } else {
        creature.body.getRootPart()
    }
    return startAttack(creature, partToAttackWith, ThingAim(GameState.player.thing, thingPart), DamageType.SLASH)
}