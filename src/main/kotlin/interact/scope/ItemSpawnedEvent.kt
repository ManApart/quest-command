package interact.scope

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class ItemSpawnedEvent(val item: Item, val target: Creature?) : Event