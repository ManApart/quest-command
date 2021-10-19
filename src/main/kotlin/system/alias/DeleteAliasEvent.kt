package system.alias

import core.Player
import core.events.Event

class DeleteAliasEvent(val source: Player, val alias: String) : Event