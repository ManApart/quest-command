package system.persistance.loading

import core.GameState
import core.events.EventListener
import core.history.display
import core.history.displayYou
import system.persistance.clean
import system.persistance.getGameNames

class ListSaves : EventListener<ListSavesEvent>() {
    override fun execute(event: ListSavesEvent) {
        val gameNames = getGameNames()
        if (gameNames.isEmpty()) {
            event.source.displayYou("No game saves to load")
        } else {
            event.source.displayYou("Game Saves:\n\t" + gameNames.joinToString("\n\t") { highlightCurrent(it, GameState.gameName) })
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