package status.conditions

import core.events.Event
import core.thing.Thing

data class RemoveConditionEvent(val thing: Thing, val condition: Condition) : Event