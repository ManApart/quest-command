package system.history

import core.Player
import core.events.Event
import core.thing.Thing

class ViewGameLogEvent(val source: Player, val numberOfLinesToShow: Int = 10, val viewResponses: Boolean = false) : Event