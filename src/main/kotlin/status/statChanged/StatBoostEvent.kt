package status.statChanged

import core.GameState
import core.events.Event
import core.target.Target

class StatBoostEvent(val target: Target = GameState.player, val source: String, val type: String, val amount: Int) : Event