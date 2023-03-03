package system.persistance.newGame

import core.GameManager
import core.GameState
import core.events.EventListener
import core.history.displayToMe
import system.persistance.clean
import system.persistance.getGameNames
import system.persistance.save

class CreateNewGame : EventListener<CreateNewGameEvent>() {
    override suspend fun complete(event: CreateNewGameEvent) {
        val gameNames = getGameNames()
        val gameName = clean(event.saveName)
        if (gameNames.map { it.lowercase() }.contains(gameName.lowercase())) {
            event.source.displayToMe("$gameName already exists!")
        } else {
            GameManager.newGame(event.saveName)
            save(GameState.gameName)
            println("Saved ${event.source.thing.name} to a new game: ${GameState.gameName}.")
        }

    }


}