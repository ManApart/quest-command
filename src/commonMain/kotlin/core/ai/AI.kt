package core.ai

import conversation.dialogue.DialogueEvent
import core.events.TemporalEvent
import core.thing.Thing

abstract class AI {
    lateinit var creature: Thing
    abstract suspend fun hear(event: DialogueEvent)
    abstract suspend fun takeAction()

    val actions = mutableListOf<TemporalEvent>()

    suspend fun chooseAction() {
        if (!creature.isPlayer()) {
            takeAction()
        }
    }


}