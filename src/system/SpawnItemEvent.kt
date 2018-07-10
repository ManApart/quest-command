package system

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class SpawnItemEvent(val itemName: String, val target: Creature? = null) : Event