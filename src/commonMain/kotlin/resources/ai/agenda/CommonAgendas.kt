package resources.ai.agenda

import combat.DamageType
import combat.attack.StartAttackEvent
import combat.attack.startAttackEvent
import core.GameState
import core.ai.agenda.AgendaResource
import core.ai.agenda.agendas
import core.ai.knowledge.DiscoverFactEvent
import core.ai.knowledge.Fact
import core.ai.knowledge.Subject
import core.thing.Thing
import core.utility.RandomManager
import status.rest.RestEvent
import traveling.location.network.LocationNode
import traveling.move.MoveEvent
import traveling.position.ThingAim
import traveling.position.Vector
import traveling.routes.FindRouteEvent
import traveling.travel.TravelStartEvent
import use.eat.EatFoodEvent
import use.interaction.InteractEvent
import use.interaction.nothing.NothingEvent

class CommonAgendas : AgendaResource {
    override val values = agendas {

        agendaAction("Attack") { creature ->
            creature.mind.getAggroTarget()?.let { target ->
                clawAttack(target, creature)
            }
        }

        agenda("Eat Food") {
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

        agenda("Travel to Location") {
            actionDetailed("Identify Route") {
                shouldSkip { s ->
                    val goal = s.mind.knowsLocationByKind("LocationGoal")
                    goal == null || s.location == goal || s.mind.route?.destination == goal
                }
                result { s ->
                    s.mind.knowsLocationByKind("LocationGoal")?.let {
                        FindRouteEvent(s, s.location, it)
                    }
                }
            }
            actionDetailed("Travel to Location") {
                shouldSkip { s ->
                    s.location == s.mind.knowsLocationByKind("LocationGoal")
                }
                result { s ->
                    s.mind.route?.let {
                        TravelStartEvent(s, destination = it.getNextStep(s.location).destination.location)
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
            val target = (owner.inventory.getItems() + owner.location.getLocation().getItems(perceivedBy = owner)).firstOrNull { it.properties.tags.has("Food") }
            target?.let {
                owner.discover(target, "useTarget")
            }
        }

        agenda("Rest") {
            action("Rest") { creature -> RestEvent(creature, 2) }
        }

        agenda("Sleep In Bed") {
            actions("Find Bed") { owner ->
                owner.mind.knowsThingByKind("MyBed")?.let { target ->
                    listOf(
                        owner.discover(target, "useTarget"),
                        owner.discover(target.location, "LocationGoal")
                    )
                }
            }
            agenda("Travel to Location")
            agenda("Move to Use Target")
            agenda("Interact Target")
        }

        agendaAction("Wander") { creature ->
            val target = creature.location.getLocation().getThings(creature).random()
            MoveEvent(creature, destination = target.position)
        }

        agenda("Travel to Job Site") {
            action("Find Work Site") { owner ->
                owner.mind.knowsLocationByKind("MyWorkplace")?.let { target ->
                    owner.discover(target, "LocationGoal")
                }
            }
            agenda("Travel to Location")
        }

        agenda("Work At Job Site") {
            action("Find Workable Activator") { s ->
                s.mind.knows("WorkTags")?.sources?.mapNotNull { it.topic }?.let { tags ->
                    s.location.getLocation().getActivators(s).firstOrNull { it.properties.tags.hasAll(tags) }?.let { target ->
                        s.discover(target, "useTarget")
                    }
                }
            }
            agenda("Move to Use Target")
            agenda("Interact Target")
        }

        agendaAction("Interact Target") { creature ->
            creature.mind.getUseTarget()?.let { target ->
                InteractEvent(creature, target)
            }
        }


    }

}

private fun Thing.discover(target: Thing, kind: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), kind))
}

private fun Thing.discover(target: LocationNode, kind: String): DiscoverFactEvent {
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
    return startAttackEvent(creature, partToAttackWith, ThingAim(GameState.player.thing, thingPart), DamageType.SLASH)
}

