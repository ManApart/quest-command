package interact.scope.spawn

import core.events.Event
import core.gameState.Creature
import core.gameState.Item
import core.gameState.location.LocationNode

class ItemSpawnedEvent(val item: Item, val target: Creature?, val targetLocation: LocationNode? = null) : Event