package core.properties.propValChanged

import core.events.Event
import core.target.Target

class PropertyStatMinnedEvent(val target: Target, val stat: String) : Event