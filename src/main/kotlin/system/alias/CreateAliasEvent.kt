package system.alias

import core.events.Event

class CreateAliasEvent(val alias: String, val command: String) : Event