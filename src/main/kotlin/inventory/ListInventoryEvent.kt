package inventory

import core.GameState
import core.events.Event
import core.target.Target

class ListInventoryEvent(val target: Target = GameState.player) : Event