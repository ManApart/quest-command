package system.debug

import core.events.Event
import core.gameState.GameState
import core.gameState.Target
import core.gameState.stat.StatKind

class DebugStatEvent(val target: Target = GameState.player, val statKind: StatKind, val statName: String, val level: Int) : Event