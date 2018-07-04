package events

import gameState.Item
import gameState.Target

class UseItemEvent(val source: Item, val target: Target) : Event