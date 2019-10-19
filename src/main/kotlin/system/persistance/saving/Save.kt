package system.persistance.saving

import core.events.EventListener
import system.GameManager

class Save : EventListener<SaveEvent>() {
    override fun execute(event: SaveEvent) {
        GameManager.saveGame()
    }
}