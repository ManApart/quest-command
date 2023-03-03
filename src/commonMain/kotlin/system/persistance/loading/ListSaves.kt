package system.persistance.loading

import core.GameState
import core.events.EventListener
import core.history.displayToMe
import system.persistance.clean
import system.persistance.getGameNames

class ListSaves : EventListener<ListSavesEvent>() {
    override suspend fun complete(event: ListSavesEvent) {
        val gameNames = getGameNames()
        if (gameNames.isEmpty()) {
            event.source.displayToMe("No game saves to load")
        } else {
            event.source.displayToMe("Game Saves:\n\t" + gameNames.joinToString("\n\t") { highlightCurrent(it, GameState.gameName) })
        }
    }

    private fun highlightCurrent(save: String, gameName: String): String {
        return if (save == clean(gameName)){
            "*$gameName"
        } else {
            save
        }
    }

}