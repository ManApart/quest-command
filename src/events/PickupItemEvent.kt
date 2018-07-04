package events

import gameState.Creature
import gameState.Item

class PickupItemEvent(val source: Creature, val item: Item) : Event