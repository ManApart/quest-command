package status

import core.events.Event
import core.thing.Thing

class ExpGainedEvent(val creature: Thing, val stat: String, val amount: Int) : Event