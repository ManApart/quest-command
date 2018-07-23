package status.statChanged

import core.events.Event
import core.gameState.Creature

class StatMaxedEvent(val creature: Creature, val stat: String) : Event