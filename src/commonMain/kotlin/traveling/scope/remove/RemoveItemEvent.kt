package traveling.scope.remove

import core.events.Event
import core.thing.Thing

data class RemoveItemEvent(val source: Thing, val item: Thing) : Event