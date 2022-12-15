package resources.ai.agenda

import combat.DamageType
import combat.attack.StartAttackEvent
import core.GameState
import core.ai.agenda.AgendaResource
import core.ai.agenda.agendas
import core.utility.RandomManager
import traveling.position.ThingAim
import use.interaction.nothing.NothingEvent

class CommonAgendas : AgendaResource {
    override val values = agendas {
        agenda("Nothing") {
            action("Nothing") { creature, _ -> NothingEvent(creature) }
        }

//        agenda("FindEnemy") {
//            action("FindEnemy") { _, context ->
//                 { source, c -> c.things("creatures", source)?.firstOrNull { !it.properties.tags.has("Predator") } }
        //Event for source to learn a fact of where the enemy is
//            }
//        }

        agenda("FindAndAttack") {
//            agenda("FindEnemy")
            action("Attack") { owner, context ->
                //Instead of context, use what player knows
                //TODO -remove context and use knowledge instead
                val target = context.thing("target", owner)!!
                val playerBody = target.body
                val possibleParts = listOf(
                    playerBody.getPart("Right Foot"),
                    playerBody.getPart("Left Foot")
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
}