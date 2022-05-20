package inventory

import core.Player
import core.events.Event
import core.thing.Thing

data class ViewInventoryEvent(val source: Player, val target: Thing) : Event