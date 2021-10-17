package system.persistance.loading

import core.Player
import core.events.Event
import core.thing.Thing

class LoadEvent(val source: Player, val saveName: String) : Event