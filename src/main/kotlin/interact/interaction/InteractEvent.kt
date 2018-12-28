package interact.interaction

import core.events.Event
import core.gameState.GameState
import core.gameState.Target

class InteractEvent(val source: Target = GameState.player, val target: Target) : Event {
    override fun gameTicks(): Int {
        return 1
    }
}