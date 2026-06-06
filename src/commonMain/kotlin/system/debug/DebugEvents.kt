package system.debug

import core.events.Event
import core.thing.Thing
import status.stat.StatKind

class DebugListEvent(val source: Thing) : Event

class DebugOptionsEvent(val source: Thing) : Event

class DebugStatEvent(val thing: Thing, val statKind: StatKind, val statName: String, val level: Int) : Event

class DebugTagEvent(val thing: Thing, val tag: String, val isAddingTag: Boolean) : Event

class DebugToggleEvent(val source: Thing, val debugTypes: List<DebugType>, val toggledOn: Boolean) : Event {
    constructor(source: Thing, debugType: DebugType, toggledOn: Boolean): this(source, listOf(debugType), toggledOn)
}

class DebugWeatherEvent(val source: Thing, val weather: String) : Event