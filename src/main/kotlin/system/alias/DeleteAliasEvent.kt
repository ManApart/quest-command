package system.alias

import core.events.Event
import core.target.Target

class DeleteAliasEvent(val source: Target, val alias: String) : Event