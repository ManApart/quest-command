package system.alias

import core.events.Event
import core.thing.Thing

class CreateAliasEvent(val source: Thing, val alias: String, val command: String) : Event