package system.persistance.saving

import core.Player
import core.events.Event

class SaveEvent(val source: Player, val silent: Boolean = false) : Event