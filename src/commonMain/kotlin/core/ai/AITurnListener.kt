package core.ai

import core.events.EventListener

class AITurnListener : EventListener<AIUpdateTick>() {

    override suspend fun complete(event: AIUpdateTick) {
        directAI()
    }

}