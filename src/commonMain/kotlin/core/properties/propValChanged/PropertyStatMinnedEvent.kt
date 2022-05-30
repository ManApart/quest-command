package core.properties.propValChanged

import core.events.Event
import core.thing.Thing

class PropertyStatMinnedEvent(val thing: Thing, val stat: String) : Event