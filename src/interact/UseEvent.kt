package interact

import core.events.Event
import core.gameState.Item
import core.gameState.Target

class UseEvent(val source: Target, val target: Target) : Event