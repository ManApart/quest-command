package combat.approach

import core.events.Event
import core.gameState.Target
import core.gameState.Vector


class MoveEvent(val source: Target, val target: Vector = Vector()) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}