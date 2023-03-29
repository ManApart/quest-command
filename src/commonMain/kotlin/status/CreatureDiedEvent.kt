package status

import core.events.Event
import core.thing.Thing

data class CreatureDiedEvent(val creature: Thing) : Event