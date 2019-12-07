package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent

interface EventParser {
    fun parse(event: TriggeredEvent, parent: Target): Event
    fun className() : String

}