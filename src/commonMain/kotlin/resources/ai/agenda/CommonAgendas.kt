package resources.ai.agenda

import combat.DamageType
import combat.attack.StartAttackEvent
import core.GameState
import core.ai.agenda.AgendaResource
import core.ai.agenda.agendas
import core.ai.knowledge.DiscoverFactEvent
import core.ai.knowledge.Fact
import core.ai.knowledge.Subject
import core.thing.Thing
import core.utility.RandomManager
import status.rest.RestEvent
import traveling.move.MoveEvent
import traveling.position.ThingAim
import traveling.position.Vector
import use.eat.EatFoodEvent
import use.interaction.nothing.NothingEvent

class CommonAgendas : AgendaResource {
    override val values = agendas {

        agendaAction("Attack") { creature ->
            creature.mind.getAggroTarget()?.let { target ->
                clawAttack(target, creature)
            }
        }

        agenda("Eat Food on Ground") {
            agenda("Search For Food")
            agenda("Move to Use Target")
            agenda("Eat Targeted Food")
        }

        agendaAction("Eat Targeted Food") { creature ->
            creature.mind.getUseTarget()?.let { target ->
                EatFoodEvent(creature, target)
            }
        }

        agenda("Hunt") {
            agenda("Search For Enemy")
            agenda("Move to Aggro Target")
            agenda("Attack")
        }

        agenda("Move to Aggro Target") {
            actionDetailed("Move to Aggro Target") {
                shouldSkip { creature ->
                    creature.mind.getAggroTarget()?.position?.let {
                        creature.canReach(it)
                    }
                }
                result { creature ->
                    creature.mind.getAggroTarget()?.position?.let {
                        MoveEvent(creature, destination = it)
                    }
                }
            }
        }

        agenda("Move to Use Target") {
            actionDetailed("Move to Use Target") {
                shouldSkip { creature ->
                    creature.mind.getUseTarget()?.position?.let {
                        creature.canReach(it)
                    }
                }
                result { creature ->
                    creature.mind.getUseTarget()?.position?.let {
                        MoveEvent(creature, destination = it)
                    }
                }
            }
        }

        agenda("Nothing") {
            action("Nothing") { creature -> NothingEvent(creature) }
        }

        agenda("Scratch Tree") {
            action("Find Tree") { owner ->
                val target = owner.location.getLocation().getActivators(perceivedBy = owner).firstOrNull { it.name.contains("Tree") }
                target?.let {
                    owner.discover(target, "useTarget")
                }
            }

            agenda("Move to Use Target")

            action("Scratch Tree") { creature ->
                creature.mind.getUseTarget()?.let { target ->
                    clawAttack(target, creature)
                }
            }
        }

        agendaAction("Search For Enemy") { owner ->
            val target = owner.location.getLocation().getCreatures(perceivedBy = owner).firstOrNull { !it.properties.tags.has("Predator") }
            target?.let {
                owner.discover(target, "aggroTarget")
            }
        }

        agendaAction("Search For Food") { owner ->
            val target = owner.location.getLocation().getItems(perceivedBy = owner).firstOrNull { it.properties.tags.has("Food") }
            target?.let {
                owner.discover(target, "useTarget")
            }
        }

        agenda("Sleep On Ground") {
            action("Rest") { creature -> RestEvent(creature, 2) }
        }

        agendaAction("Wander") { creature ->
            val additional = Vector(RandomManager.getRandom(0, 10), RandomManager.getRandom(0, 10), RandomManager.getRandom(0, 10))
            MoveEvent(creature, destination = creature.position + additional)
        }

    }

}

private fun Thing.discover(target: Thing, kind: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), kind))
}

private suspend fun clawAttack(target: Thing, creature: Thing): StartAttackEvent {
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
    return StartAttackEvent(creature, partToAttackWith, ThingAim(GameState.player.thing, thingPart), DamageType.SLASH)
}

