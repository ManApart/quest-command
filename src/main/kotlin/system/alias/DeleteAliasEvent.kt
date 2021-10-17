package system.alias

import core.events.Event
import core.thing.Thing

class DeleteAliasEvent(val source: Thing, val alias: String) : Event