package core.properties

import core.events.Event
import core.target.Target

class SetPropertiesEvent(val target: Target, val properties: Properties) : Event