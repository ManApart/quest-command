package status.conditions

import core.events.Event
import core.target.Target

class AddConditionEvent(val target: Target, val condition: Condition) : Event