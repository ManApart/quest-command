package system.persistance.loading

import core.events.EventListener
import core.history.display
import system.persistance.getGameNames

class ListSaves : EventListener<ListSavesEvent>() {
    override fun execute(event: ListSavesEvent) {
        val gameNames = getGameNames()
        display("Game Saves:\n\t" + gameNames.joinToString("\n\t"))
    }

}