package interact

import core.events.Event
import core.gameState.Item
import core.gameState.Target

class UseItemEvent(val source: Item, val target: Target) : Event