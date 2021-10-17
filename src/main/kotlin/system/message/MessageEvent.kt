package system.message

import core.Player
import core.events.Event
import core.thing.Thing

class MessageEvent(val source: Player, val message: String) : Event