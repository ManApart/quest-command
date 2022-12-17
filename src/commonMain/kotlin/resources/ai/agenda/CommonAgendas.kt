package resources.ai.agenda

import combat.DamageType
import combat.attack.StartAttackEvent
import core.GameState
import core.ai.agenda.AgendaResource
import core.ai.agenda.agendas
import core.ai.knowledge.DiscoverFactEvent
import core.ai.knowledge.Fact
import core.ai.knowledge.Subject
import core.events.Event
import core.utility.RandomManager
import crafting.DiscoverRecipeEvent
import traveling.position.ThingAim
import use.interaction.nothing.NothingEvent

class CommonAgendas : AgendaResource {
    override val values = agendas {
        agenda("Nothing") {
            action("Nothing") { creature, _ -> NothingEvent(creature) }
        }

        agenda("SearchForEnemy") {
            action("SearchForEnemy") { owner, _ ->
                val enemy = owner.location.getLocation().getCreatures(perceivedBy = owner).firstOrNull { !it.properties.tags.has("Predator") }
                enemy?.let {
                    DiscoverFactEvent(owner, Fact(Subject(enemy), "target"))
                }
            }
        }

        agenda("Attack"){
            action("Attack") { owner, _ ->
                owner.mind.knowsThingByKind("target")?.let { target ->
                    val enemyBody = target.body
                    val possibleParts = listOf(
                        enemyBody.getPart("Right Foot"),
                        enemyBody.getPart("Left Foot")
                    )
                    val thingPart = listOf(RandomManager.getRandom(possibleParts))
                    val partToAttackWith = if (owner.body.hasPart("Small Claws")) {
                        owner.body.getPart("Small Claws")
                    } else {
                        owner.body.getRootPart()
                    }
                    StartAttackEvent(owner, partToAttackWith, ThingAim(GameState.player.thing, thingPart), DamageType.SLASH)
                }
            }
        }

        agenda("FindAndAttack") {
            agenda("SearchForEnemy")
            agenda("Attack")
        }
    }
}