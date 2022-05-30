package system.persistance.saving

import core.GameState
import core.events.EventListener
import core.history.displayGlobal
import system.persistance.save

class Save : EventListener<SaveEvent>() {
    override fun execute(event: SaveEvent) {
        save(GameState.gameName)

        if (!event.silent) {
            displayGlobal("Saved ${event.source.thing.name} in ${GameState.gameName}.")
        }
    }
}