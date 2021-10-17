package status

import core.events.Event
import core.thing.Thing

class CreatureDiedEvent(val creature: Thing) : Event