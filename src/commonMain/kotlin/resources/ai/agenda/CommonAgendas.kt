package resources.ai.agenda

import combat.DamageType
import combat.attack.StartAttackEvent
import core.GameState
import core.ai.agenda.Agenda
import core.ai.agenda.AgendaResource
import core.conditional.Context
import core.events.Event
import core.thing.Thing
import core.utility.RandomManager
import traveling.position.ThingAim

class CommonAgendas : AgendaResource {
    override val values = listOf(
        Agenda("FindAndAttack", listOf())
    )
}

private fun ratAttack(owner: Thing, context: Context): Event {
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
    return StartAttackEvent(owner, partToAttackWith, ThingAim(GameState.player.thing, thingPart), DamageType.SLASH)
}