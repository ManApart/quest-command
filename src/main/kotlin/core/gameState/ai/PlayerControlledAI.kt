package core.gameState.ai

import core.commands.CommandParser
import core.gameState.GameState
import core.gameState.Target
import core.history.display

const val PLAYER_CONTROLLED_ID = "Player Controlled"

class PlayerControlledAI(creature: Target) : AI(PLAYER_CONTROLLED_ID, creature) {

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