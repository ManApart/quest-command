package system.persistance.saving

import core.GameState
import core.events.EventListener
import system.persistance.save

class Save : EventListener<SaveEvent>() {
    override fun execute(event: SaveEvent) {
        save(GameState.gameName, event.source)

        println("Saved ${event.source.name} in ${GameState.gameName}.")
    }


}