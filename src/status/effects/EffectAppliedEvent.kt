package status.effects

import core.events.Event
import core.gameState.Creature
import core.gameState.Effect

class EffectAppliedEvent(val creature: Creature, val effect: Effect) : Event {
    override fun gameTicks(): Int {
        return 0
    }
}