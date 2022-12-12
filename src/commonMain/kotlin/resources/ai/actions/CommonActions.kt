package resources.ai.actions

import combat.DamageType
import combat.attack.StartAttackEvent
import core.GameState
import core.conditional.Context
import core.ai.action.dsl.AIActionResource
import core.ai.action.dsl.actions
import core.events.Event
import core.thing.Thing
import core.utility.RandomManager
import traveling.position.ThingAim
import use.interaction.nothing.NothingEvent

class CommonActions : AIActionResource {
    override val values = actions {
        action("Nothing") { creature, _ -> NothingEvent(creature) }

        context("creatures") { source, _ -> source.location.getLocation().getCreatures(perceivedBy = source).filter { it != source } }

        cond({ source, _ -> source.properties.tags.has("Predator") }) {
            //Eventually use factions + actions to create how much something likes something else
            context("target") { source, c -> c.things("creatures", source)?.firstOrNull { !it.properties.tags.has("Predator") } }
            cond({ s, c -> c.thing("target", s) != null }) {
                action("Rat Attack", ::ratAttack)
            }
        }
    }
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