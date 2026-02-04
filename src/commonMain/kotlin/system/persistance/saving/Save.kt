package system.persistance.saving

import core.GameState
import core.events.EventListener
import core.history.displayGlobal
import system.persistance.save

class Save : EventListener<SaveEvent>() {
    override suspend fun complete(event: SaveEvent) {
        save(GameState.gameName.lowercase())

        if (!event.silent) {
            displayGlobal("Saved ${event.source.thing.name} in ${GameState.gameName.lowercase()}.")
        }
    }
}
