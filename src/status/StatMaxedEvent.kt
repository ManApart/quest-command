package status

import core.events.Event
import core.gameState.Creature
import core.gameState.Stat

class StatMaxedEvent(val creature: Creature, val stat: String) : Event