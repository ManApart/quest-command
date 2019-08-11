package status.statChanged

import core.events.Event
import core.gameState.GameState
import core.gameState.Target
//add damage type
class StatChangeEvent(val target: Target = GameState.player, val source: String, val type: String, val amount: Int) : Event