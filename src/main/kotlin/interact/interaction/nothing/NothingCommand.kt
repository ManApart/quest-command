package interact.interaction.nothing

import core.commands.Command
import core.gameState.GameState
import core.gameState.Target
import system.EventManager

class NothingCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Nothing")
    }

    override fun getDescription(): String {
        return "Nothing\n\tLike resting, but less useful"
    }

    override fun getManual(): String {
        return "\n\tNothing - Do Nothing"
    }

    override fun getCategory(): List<String> {
        return listOf("Interact")
    }

    override fun execute(keyword: String, args: List<String>) {
        execute(GameState.player, keyword, args)
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        if (GameState.battle == null) {
            EventManager.postEventAndSetPlayerTurn(NothingEvent(source))
        } else {
            EventManager.postEventAndSetPlayerTurn(StartNothingEvent(source))
        }
    }

}