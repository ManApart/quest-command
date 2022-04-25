package core.ai

import conversation.dialogue.DialogueEvent
import core.history.display
import core.history.displayToMe
import core.history.displayToOthers

const val PLAYER_CONTROLLED_ID = "Player Controlled"

class PlayerControlledAI : AI(PLAYER_CONTROLLED_ID) {
    override fun hear(event: DialogueEvent) {
        event.speaker.display("" + event.speaker.name + ": " + event.line)
    }

    override fun takeAction() {
        creature.displayToMe("What do you do, ${creature.name}?")
        creature.displayToOthers("${creature.name} does what?")
    }

}