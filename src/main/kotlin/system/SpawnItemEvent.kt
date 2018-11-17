package system

import core.events.Event
import core.gameState.Creature

class SpawnItemEvent(val itemName: String, val count: Int = 1, val target: Creature? = null) : Event