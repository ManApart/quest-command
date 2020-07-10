package traveling.move

import core.events.Event
import core.GameState
import core.target.Target
import traveling.direction.Vector

class MoveEvent(val creature: Target = GameState.player, val source: Vector = creature.position, val destination: Vector, val useStamina: Boolean = false, val silent: Boolean = false) : Event {
    override fun gameTicks(): Int {
        return if (creature.isPlayer()) {
            1
        } else {
            0
        }
    }
}