package processing

import events.ClimbEvent
import events.EventListener
import gameState.Activator
import gameState.Target

object ClimbManager {

    class ClimbHandler : EventListener<ClimbEvent>() {
        override fun execute(event: ClimbEvent) {
            println("You climb ${event.target.name}")
            if (event.target is Activator){
                event.target.evaluateAndExecute(event)
            }
        }
    }

    fun attemptToClimb(target: Target) {
        //TODO - add skill checks
        EventManager.postEvent(ClimbEvent(target))
    }
}