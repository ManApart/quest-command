package inventory

import core.Player
import core.events.Event
import core.thing.Thing

data class ViewInventoryFitEvent(val source: Player, val taker: Thing, val item: Thing) : Event
