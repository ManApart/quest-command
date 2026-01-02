package core.ai

import conversation.dialogue.DialogueEvent
import core.events.TemporalEvent
import core.thing.Thing

abstract class AI {
    lateinit var creature: Thing
    var enabled: Boolean = true
    var takenTurn: Boolean = false
    abstract suspend fun hear(event: DialogueEvent)
    abstract suspend fun takeAction(): Boolean
    val actions = mutableListOf<TemporalEvent>()

    suspend fun chooseAction() {
        if (enabled && !creature.isPlayer()) {
            takenTurn = takeAction()
        }
    }
}
