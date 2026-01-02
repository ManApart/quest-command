package core.ai

import conversation.dialogue.DialogueEvent
import core.history.display
import core.history.displayToMe
import core.history.displayToOthersGlobal

class PlayerControlledAI : AI() {
    override fun toString(): String {
        return "Player Controlled AI for ${creature.name}"
    }
    override suspend fun hear(event: DialogueEvent) {
        event.speaker.display("" + event.speaker.name + ": " + event.line)
    }

    override suspend fun takeAction(): Boolean {
        creature.displayToMe("What do you do, ${creature.name}?")
        creature.displayToOthersGlobal("${creature.name} does what?")
        return true
    }

}