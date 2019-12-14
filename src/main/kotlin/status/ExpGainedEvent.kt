package status

import core.events.Event
import core.target.Target

class ExpGainedEvent(val creature: Target, val stat: String, val amount: Int) : Event