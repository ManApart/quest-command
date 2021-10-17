package core.ai

import conversation.dialogue.DialogueEvent
import core.GameState
import core.commands.CommandParser
import core.history.display

const val PLAYER_CONTROLLED_ID = "Player Controlled"

class PlayerControlledAI : AI(PLAYER_CONTROLLED_ID) {
    override fun hear(event: DialogueEvent) {
        event.speaker.display("" + event.speaker.name + ": " + event.line)
    }

    override fun takeAction() {
        val oldCreature = CommandParser.commandSource
        val newCreature = GameState.getPlayer(creature)
        CommandParser.commandSource = newCreature

        if (newCreature != oldCreature) {
            if (newCreature.thing.isPlayer()) {
                newCreature.display("What do you do?")
            } else {
                newCreature.display("${creature.name} does what?")
            }
        }
    }

}