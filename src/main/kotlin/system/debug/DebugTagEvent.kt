package system.debug

import core.events.Event
import core.GameState
import core.target.Target

class DebugTagEvent(val target: Target = GameState.player, val tag: String, val isAddingTag: Boolean) : Event