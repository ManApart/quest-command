package traveling.move

import core.events.Event
import core.GameState
import core.target.Target
import traveling.position.Vector

class MoveEvent(val creature: Target = GameState.player, val source: Vector = creature.position, val destination: Vector, val staminaScalar: Float = 1f, val silent: Boolean = false) : Event {

    //TODO - why only if it's the player does it tick?
    override fun gameTicks(): Int {
        return if (creature.isPlayer()) {
            1
        } else {
            0
        }
    }
}