package combat.slash

import combat.TargetDirection
import core.events.Event
import core.gameState.body.BodyPart
import core.gameState.Creature
import core.gameState.GameState
import core.gameState.Target

class SlashEvent(val source: Creature, val sourcePart: BodyPart, val target: Target, val direction: TargetDirection) : Event {
    override fun gameTicks(): Int {
        return if (source == GameState.player.creature) {
            1
        } else {
            0
        }
    }
}