package system.debug

import core.GameState
import core.events.Event
import core.target.Target
import status.stat.StatKind

class DebugStatEvent(val target: Target = GameState.player, val statKind: StatKind, val statName: String, val level: Int) : Event