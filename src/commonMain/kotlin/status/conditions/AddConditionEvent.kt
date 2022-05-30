package status.conditions

import core.events.Event
import core.thing.Thing

class AddConditionEvent(val thing: Thing, val condition: Condition, val silent: Boolean = false) : Event