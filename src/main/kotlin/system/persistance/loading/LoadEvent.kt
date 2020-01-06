package system.persistance.loading

import core.events.Event

class LoadEvent(val saveName: String? = null, val list: Boolean = false) : Event