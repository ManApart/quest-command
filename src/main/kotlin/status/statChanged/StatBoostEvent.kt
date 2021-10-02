package status.statChanged

import core.events.Event
import core.target.Target

class StatBoostEvent(val target: Target, val source: String, val type: String, val amount: Int) : Event