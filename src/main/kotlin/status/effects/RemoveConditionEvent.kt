package status.effects

import core.events.Event
import core.gameState.Target

class RemoveConditionEvent(val target: Target, val condition: Condition) : Event