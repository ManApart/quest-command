package inventory

import core.events.Event
import core.gameState.Target
import core.gameState.GameState

class ListInventoryEvent(val target: Target = GameState.player) : Event