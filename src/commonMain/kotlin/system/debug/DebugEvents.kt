package system.debug

import core.events.Event
import core.thing.Thing
import status.stat.StatKind

class DebugListEvent(val source: Thing) : Event

class DebugOptionsEvent(val source: Thing) : Event

data class DebugStatEvent(val thing: Thing, val statKind: StatKind, val statName: String, val level: Int) : Event

data class DebugTagEvent(val thing: Thing, val tag: String, val isAddingTag: Boolean) : Event

data class DebugToggleEvent(val source: Thing, val debugTypes: List<DebugType>, val toggledOn: Boolean) : Event

class DebugWeatherEvent(val source: Thing, val weather: String) : Event