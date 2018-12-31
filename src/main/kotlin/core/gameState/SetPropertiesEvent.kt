package core.gameState

import core.events.Event

class SetPropertiesEvent(val target: Target, val properties: Properties) : Event