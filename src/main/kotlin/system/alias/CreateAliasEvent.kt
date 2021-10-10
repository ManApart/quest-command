package system.alias

import core.events.Event
import core.target.Target

class CreateAliasEvent(val source: Target, val alias: String, val command: String) : Event