package status

import core.events.Event
import core.gameState.Stat

class StatMinnedEvent(val stat: Stat.StatType) : Event