package explore.look

import core.GameState
import core.Player
import core.ai.ConditionalAI
import core.ai.DumbAI
import core.ai.PlayerControlledAI
import core.ai.knowledge.Mind
import core.history.displayToMe
import core.thing.Thing
import system.debug.DebugType

fun describeMind(source: Player, thing: Thing, mind: Mind) {
    when {
        mind.ai is PlayerControlledAI || thing.isPlayer() -> source.displayToMe("Who can fathom the thought of ${thing.getDisplayName()}?")
        mind.ai is DumbAI -> source.displayToMe("${thing.getDisplayName()} is without thought.")
        !GameState.getDebugBoolean(DebugType.CLARITY) -> source.displayToMe("You are unable to perceive the thought of ${thing.getDisplayName()}.")
        mind.ai is ConditionalAI -> ponderThing(source, thing, mind.ai)
        else -> source.displayToMe("You are unable to perceive the thought of ${thing.getDisplayName()}.")
    }
}

private fun ponderThing(source: Player, thing: Thing, ai: ConditionalAI) {
    var message = thing.getDisplayName()
    with(ai) {
        if (goal == null) {
            message += "\n\thas no goal"
        } else {
            message += "\n\tis working towards ${goal?.name ?: ""}"
            message += "\n\tis on step ${goal?.progress}: ${goal?.steps?.get(goal?.progress ?: 0)?.name ?: ""}"
        }
        source.displayToMe(message)
    }
}