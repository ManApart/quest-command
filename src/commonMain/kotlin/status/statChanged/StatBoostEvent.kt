package status.statChanged

import core.events.Event
import core.thing.Thing

class StatBoostEvent(val thing: Thing, val source: String, val type: String, val amount: Int) : Event