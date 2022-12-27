package core.ai

import core.events.EventListener
import traveling.arrive.ArriveEvent

class AIArriveListener : EventListener<ArriveEvent>() {

    override fun getPriorityRank() = 20

    override fun execute(event: ArriveEvent) {
        directAI()
    }

}