package core.ai

import core.events.EventListener

class AITurnListener : EventListener<AIUpdateTick>() {

    override fun execute(event: AIUpdateTick) {
        directAI()
    }

}