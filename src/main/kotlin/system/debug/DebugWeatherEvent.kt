package system.debug

import core.events.Event
import core.target.Target

class DebugWeatherEvent(val source: Target, val weather: String) : Event