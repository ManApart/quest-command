package system.debug

import core.events.Event
import core.gameState.GameState
import core.gameState.Target
import core.gameState.stat.StatKind

class DebugTagEvent(val target: Target = GameState.player, val tag: String, val isAddingTag: Boolean) : Event