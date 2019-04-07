package combat.dodge

import combat.battle.position.TargetDirection
import core.events.Event
import core.gameState.Creature
import core.gameState.isPlayer

class DodgeEvent(val source: Creature, val direction: TargetDirection) : Event {
    override fun gameTicks(): Int {
        return if (source.isPlayer()) {
            1
        } else {
            0
        }
    }
}