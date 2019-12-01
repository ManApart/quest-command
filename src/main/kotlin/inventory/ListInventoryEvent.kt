package inventory

import core.events.Event
import core.gameState.GameState
import core.gameState.Target

class ListInventoryEvent(val target: Target = GameState.player) : Event