package status

import core.events.Event
import core.gameState.Stat

class StatMaxedEvent(val stat: Stat.StatType) : Event