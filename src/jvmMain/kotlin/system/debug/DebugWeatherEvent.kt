package system.debug

import core.events.Event
import core.thing.Thing

class DebugWeatherEvent(val source: Thing, val weather: String) : Event