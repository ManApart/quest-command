package system.persistance.loading

import core.events.Event

class LoadEvent(val loadGame: Boolean, val saveName: String) : Event