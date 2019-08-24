package status.statChanged

import core.events.Event
import core.gameState.GameState
import core.gameState.Target

class StatChangeEvent(val target: Target = GameState.player, val sourceOfChange: String, val statName: String, val amount: Int, val silent: Boolean = false) : Event