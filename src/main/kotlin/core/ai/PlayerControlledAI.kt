package core.ai

import conversation.dialogue.DialogueEvent
import core.commands.CommandParser
import core.history.display

const val PLAYER_CONTROLLED_ID = "Player Controlled"

class PlayerControlledAI : AI(PLAYER_CONTROLLED_ID) {
    override fun hear(event: DialogueEvent) {
        event.speaker.display("" + event.speaker.name + ": " + event.line)
    }

    override fun takeAction() {
        val oldCreature = CommandParser.commandSource
        CommandParser.commandSource = creature

        if (creature != oldCreature) {
            if (creature.isPlayer()) {
                creature.display("What do you do?")
            } else {
                creature.display("${creature.name} does what?")
            }
        }
    }

}