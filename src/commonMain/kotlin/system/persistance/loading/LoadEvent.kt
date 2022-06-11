package system.persistance.loading

import core.Player
import core.events.Event

class LoadEvent(val source: Player, val saveName: String) : Event