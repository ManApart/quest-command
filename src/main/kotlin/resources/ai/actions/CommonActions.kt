package resources.ai.actions

import combat.DamageType
import combat.attack.StartAttackEvent
import core.GameState
import core.ai.action.AIAction
import core.ai.action.dsl.AIActionResource
import core.ai.action.dsl.actions
import core.events.Event
import core.target.Target
import core.utility.RandomManager
import traveling.position.TargetAim
import use.interaction.nothing.NothingEvent

class CommonActions : AIActionResource {
    private val defaultAction = AIAction("Nothing", listOf(), { listOf(NothingEvent(it)) }, 1)
    override val values = actions {
        action("Rat Attack", ::ratAttack)

    }.build() + listOf(defaultAction)
}

private fun ratAttack(owner: Target): Event {
    val playerBody = GameState.player.body
    val possibleParts = listOf(
        playerBody.getPart("Right Foot"),
        playerBody.getPart("Left Foot")
    )
    val targetPart = listOf(RandomManager.getRandom(possibleParts))
    val partToAttackWith = if (owner.body.hasPart("Small Claws")) {
        owner.body.getPart("Small Claws")
    } else {
        owner.body.getRootPart()
    }
    return StartAttackEvent(owner, partToAttackWith, TargetAim(GameState.player, targetPart), DamageType.SLASH)
}