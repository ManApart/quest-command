package system.persistance.saving

import core.GameState
import core.events.EventListener
import system.persistance.save

class Save : EventListener<SaveEvent>() {
    override fun execute(event: SaveEvent) {
        save(GameState.gameName, GameState.player)

        println("Saved ${GameState.player.name} in ${GameState.gameName}.")
    }


}