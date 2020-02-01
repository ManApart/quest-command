package explore.examine

import core.GameState
import core.events.Event
import core.target.Target

class ExamineEvent(val source: Target = GameState.player, val target: Target? = null) : Event