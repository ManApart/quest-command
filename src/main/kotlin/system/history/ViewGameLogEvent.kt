package system.history

import core.Player
import core.events.Event

class ViewGameLogEvent(val source: Player, val numberOfLinesToShow: Int = 10, val viewResponses: Boolean = false) : Event