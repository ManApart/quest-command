package status.effects

import core.events.Event
import core.gameState.Target

class AddConditionEvent(val target: Target, val condition: Condition) : Event