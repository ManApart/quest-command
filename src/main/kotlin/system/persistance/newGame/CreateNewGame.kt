package system.persistance.newGame

import core.GameManager
import core.GameState
import core.events.EventListener
import core.history.display
import system.persistance.clean
import system.persistance.getGameNames
import system.persistance.save

class CreateNewGame : EventListener<CreateNewGameEvent>() {
    override fun execute(event: CreateNewGameEvent) {
        val gameNames = getGameNames()
        val gameName = clean(event.saveName)
        if (gameNames.map { it.lowercase() }.contains(gameName.lowercase())) {
            display("$gameName already exists!")
        } else {
            GameManager.newGame(event.saveName)
            save(GameState.gameName, GameState.player)
            println("Saved ${GameState.player.name} to a new game: ${GameState.gameName}.")
        }

    }


}