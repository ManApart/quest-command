package core.ai

import conversation.dialogue.DialogueEvent
import core.commands.CommandParser
import core.GameState
import core.target.Target
import core.history.display

const val PLAYER_CONTROLLED_ID = "Player Controlled"

class PlayerControlledAI(creature: Target) : AI(PLAYER_CONTROLLED_ID, creature) {
    override fun hear(event: DialogueEvent) {
        display("" + event.speaker + ": " + event.line)
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