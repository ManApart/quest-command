package interact.scope.remove

import core.events.Event
import core.gameState.GameState
import core.gameState.Target

class RemoveItemEvent(val source: Target = GameState.player, val item: Target) : Event