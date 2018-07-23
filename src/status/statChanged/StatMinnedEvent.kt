package status.statChanged

import core.events.Event
import core.gameState.Creature

class StatMinnedEvent(val creature: Creature, val stat: String) : Event