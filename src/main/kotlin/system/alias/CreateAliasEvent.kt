package system.alias

import core.Player
import core.events.Event
import core.thing.Thing

class CreateAliasEvent(val source: Player, val alias: String, val command: String) : Event