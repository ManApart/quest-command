package traveling.scope.remove

import core.events.Event
import core.GameState
import core.target.Target

class RemoveItemEvent(val source: Target = GameState.player, val item: Target) : Event