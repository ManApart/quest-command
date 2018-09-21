package status.effects

import core.events.Event
import core.gameState.Effect
import core.gameState.Target

class EffectAppliedEvent(val target: Target, val effect: Effect) : Event {
    override fun gameTicks(): Int {
        return 0
    }
}