package magic

import core.events.Event

class ViewWordHelpEvent(val word: String? = null, val groups: Boolean = false) : Event