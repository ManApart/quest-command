package events

import gameState.Creature
import gameState.Item

class DropItemEvent(val source: Creature, val item: Item) : Event