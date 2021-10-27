package system.alias

import core.Player
import core.events.Event

class CreateAliasEvent(val source: Player, val alias: String, val command: String) : Event