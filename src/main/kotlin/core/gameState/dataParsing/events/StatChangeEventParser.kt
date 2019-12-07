package core.gameState.dataParsing.events

import core.events.Event
import core.gameState.Target
import core.gameState.dataParsing.TriggeredEvent
import status.statChanged.StatChangeEvent

class StatChangeEventParser : EventParser {
    override fun className(): String {
        return StatChangeEvent::class.simpleName!!
    }

    override fun parse(event: TriggeredEvent, parent: Target): Event {
        val targetP = 0
        val sourceOfChangeP = 1
        val statNameP = 2
        val amountP = 3

        val target = event.getTargetCreatureOrPlayer(parent, targetP)
        val sourceOfChange = event.getParam(sourceOfChangeP, "event")
        val statName = event.getParam(statNameP, "none")
        val amount = event.getParamInt(amountP, 0)

        return StatChangeEvent(target, sourceOfChange, statName, amount)
    }
}