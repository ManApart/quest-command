package inventory

import core.events.Event
import core.GameState
import core.target.Target

class ListInventoryEvent(val target: Target = GameState.player) : Event