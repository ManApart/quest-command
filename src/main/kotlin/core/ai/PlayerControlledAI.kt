package core.ai

import conversation.dialogue.DialogueEvent
import core.GameState
import core.commands.CommandParser
import core.history.display
import core.target.Target

const val PLAYER_CONTROLLED_ID = "Player Controlled"

class PlayerControlledAI : AI(PLAYER_CONTROLLED_ID) {
    override fun hear(event: DialogueEvent) {
        display("" + event.speaker.name + ": " + event.line)
    }

    override fun takeAction() {
        val oldCreature = CommandParser.commandSource ?: GameState.player
        CommandParser.commandSource = creature

        if (creature != oldCreature) {
            if (creature.isPlayer()) {
                display("What do you do?")
            } else {
                display("${creature.name} does what?")
            }
        }
    }

}