package core.properties

import core.events.Event
import core.thing.Thing

class SetPropertiesEvent(val thing: Thing, val properties: Properties) : Event