package system.message

import core.Player
import core.events.Event

data class MessageEvent(val source: Player, val message: String) : Event