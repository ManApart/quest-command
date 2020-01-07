package system.persistance.loading

import core.GameState
import core.events.EventListener
import core.history.display
import system.persistance.getGameNames
import system.persistance.getSaveNames

class ListSaves : EventListener<ListSavesEvent>() {
    override fun execute(event: ListSavesEvent) {
        if (event.listGameNames) {
            val gameNames = getGameNames()
            display("Game Saves:\n\t" + gameNames.joinToString("\n\t"))
        } else {
            val saveNames = getSaveNames(GameState.gameName)
            display("Character Saves in ${GameState.gameName}:\n\t" + saveNames.joinToString("\n\t"))
        }
    }


}