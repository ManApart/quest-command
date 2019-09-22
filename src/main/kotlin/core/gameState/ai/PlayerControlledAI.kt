package core.gameState.ai

import core.commands.CommandParser
import core.gameState.Target
import core.history.display

const val PLAYER_CONTROLLED_ID = "Player Controlled"

class PlayerControlledAI(creature: Target) : AI(PLAYER_CONTROLLED_ID, creature) {

    override fun takeAction() {
        CommandParser.commandSource = creature
        if (!creature.isPlayer()) {
            display("${creature.name} does what?")
        }
    }

}