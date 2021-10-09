package magic

import core.events.Event
import core.target.Target

class ViewWordHelpEvent(val source: Target, val word: String? = null, val groups: Boolean = false) : Event