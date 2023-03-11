package core.events

import time.gameTick.GameTickEvent

class EventManagerTickListener : EventListener<GameTickEvent>() {

    override suspend fun complete(event: GameTickEvent) {
        EventManager.tick(event.time)
    }

}