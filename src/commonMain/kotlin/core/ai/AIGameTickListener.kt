package core.ai

import core.events.EventListener
import core.events.EventManager
import time.gameTick.GameTickEvent

class AIGameTickListener : EventListener<GameTickEvent>() {
    override suspend fun complete(event: GameTickEvent) {
        EventManager.postEvent(AIUpdateTick())
    }
}