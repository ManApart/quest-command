package resources.ai.agenda

import combat.DamageType
import combat.attack.StartAttackEvent
import core.GameState
import core.ai.agenda.AgendaResource
import core.ai.agenda.agendas
import core.conditional.Context
import core.events.Event
import core.thing.Thing
import core.utility.RandomManager
import traveling.position.ThingAim
import use.interaction.nothing.NothingEvent

class CommonAgendas : AgendaResource {
    override val values = agendas {
        agenda("Nothing") {
            action("Nothing") { creature, _ -> NothingEvent(creature) }
        }

        agenda("FindAndAttack") {
            action("Attack") { owner, context ->
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