package traveling.scope.remove

import core.events.Event
import core.target.Target

class RemoveItemEvent(val source: Target, val item: Target) : Event