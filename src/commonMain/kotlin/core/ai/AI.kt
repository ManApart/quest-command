package core.ai

import conversation.dialogue.DialogueEvent
import core.events.Event
import core.thing.Thing

abstract class AI {
    lateinit var creature: Thing
    abstract suspend fun hear(event: DialogueEvent)
    abstract suspend fun takeAction()

    val actions = mutableListOf<Event>()

    suspend fun chooseAction() {
        if (!creature.isPlayer()) {
            takeAction()
        }
    }


}