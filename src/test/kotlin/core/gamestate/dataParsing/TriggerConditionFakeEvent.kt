package core.gamestate.dataParsing

import core.events.Event
import core.gameState.Direction

data class TriggerConditionFakeEvent(val stringVal: String = "StringValue", val intVal: Int= 0, val boolVal: Boolean = false, val classVal: Direction = Direction.ABOVE) : Event