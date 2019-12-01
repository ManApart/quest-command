package system.debug

import core.events.Event
import core.gameState.GameState
import core.gameState.Target

class DebugTagEvent(val target: Target = GameState.player, val tag: String, val isAddingTag: Boolean) : Event