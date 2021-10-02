package system.persistance.loading

import core.events.Event
import core.target.Target

class LoadEvent(val source: Target, val saveName: String) : Event