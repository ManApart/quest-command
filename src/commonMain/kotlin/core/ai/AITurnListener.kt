package core.ai

import core.events.EventListener

class AITurnListener : EventListener<AIUpdateTick>() {

    override suspend fun execute(event: AIUpdateTick) {
        directAI()
    }

}