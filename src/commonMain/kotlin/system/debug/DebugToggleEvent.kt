package system.debug

import core.events.Event
import core.thing.Thing

class DebugToggleEvent(val source: Thing, val debugTypes: List<DebugType>, val toggledOn: Boolean) : Event {
    constructor(source: Thing, debugType: DebugType, toggledOn: Boolean): this(source, listOf(debugType), toggledOn)
}