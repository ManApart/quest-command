package resources.ai.actions

import combat.DamageType
import combat.attack.StartAttackEvent
import core.GameState
import core.ai.action.AIAction
import core.conditional.Context
import core.ai.action.dsl.AIActionResource
import core.ai.action.dsl.actions
import core.events.Event
import core.thing.Thing
import core.utility.RandomManager
import traveling.position.ThingAim
import use.interaction.nothing.NothingEvent

class CommonActions : AIActionResource {
    private val defaultAction = AIAction("Nothing", Context(), listOf(), { creature, _ -> listOf(NothingEvent(creature)) }, 1)
    override val values = actions {
        context("creatures") {source, _ -> source.location.getLocation().getCreatures(perceivedBy = source)}
        context("target") {source, c -> c.things("creatures")?.firstOrNull{ source.mind.knows(source, "likes", it).amount < 10 }}

        cond({ _, c -> c.thing("target") != null }) {
            action("Rat Attack", ::ratAttack)
        }

    } + listOf(defaultAction)
}

private fun ratAttack(owner: Thing, context: Context): Event {
    val target = context.thing("target")!!
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