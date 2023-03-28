package traveling.scope.remove

import core.events.Event
import core.thing.Thing

data class RemoveScopeEvent(val thing: Thing) : Event