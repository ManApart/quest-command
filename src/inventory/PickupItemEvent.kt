package inventory

import core.events.Event
import core.gameState.Creature
import core.gameState.Item

class PickupItemEvent(val source: Creature, val item: Item) : Event