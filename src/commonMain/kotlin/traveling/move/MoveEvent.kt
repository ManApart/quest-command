package traveling.move

import core.events.Event
import core.thing.Thing
import traveling.position.Vector

class MoveEvent(val creature: Thing, val source: Vector = creature.position, val destination: Vector, val staminaScalar: Float = 1f, val silent: Boolean = false) : Event {

    override fun gameTicks(): Int {
        return 1
    }
}