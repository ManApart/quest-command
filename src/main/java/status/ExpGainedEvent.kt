package status

import core.events.Event
import core.gameState.Creature

class ExpGainedEvent(val creature: Creature, val stat: String, val amount: Int) : Event