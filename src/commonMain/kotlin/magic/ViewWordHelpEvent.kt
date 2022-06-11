package magic

import core.events.Event
import core.thing.Thing

class ViewWordHelpEvent(val source: Thing, val word: String? = null, val groups: Boolean = false) : Event