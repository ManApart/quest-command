package core.ai

import conversation.dialogue.DialogueEvent
import core.history.display
import core.history.displayToMe
import core.history.displayToOthersGlobal

class PlayerControlledAI : AI() {
    override suspend fun hear(event: DialogueEvent) {
        event.speaker.display("" + event.speaker.name + ": " + event.line)
    }

    override suspend fun takeAction() {
        creature.displayToMe("What do you do, ${creature.name}?")
        creature.displayToOthersGlobal("${creature.name} does what?")
    }

}