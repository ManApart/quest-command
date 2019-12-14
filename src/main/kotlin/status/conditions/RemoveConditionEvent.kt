package status.conditions

import core.events.Event
import core.target.Target

class RemoveConditionEvent(val target: Target, val condition: Condition) : Event