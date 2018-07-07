package travel.climb

import core.events.EventListener
import core.gameState.consume
import core.utility.StringFormatter
import system.EventManager

object ClimbManager {

    class ClimbCompleteHandler : EventListener<ClimbCompleteEvent>() {
        override fun execute(event: ClimbCompleteEvent) {
            if (event.succces) {
                println("You climb ${StringFormatter.format(event.upwards, "up", "down")} ${event.target.name}")
            } else {
                println("You fail to climb ${event.target.name}")
            }
            event.target.consume(event)
        }
    }

    class ClimbAttemptHandler : EventListener<ClimbAttemptEvent>() {
        override fun execute(event: ClimbAttemptEvent) {
            println("You attempt to climb ${event.target.name}")
            //TODO - skill check for success or not
            EventManager.postEvent(ClimbCompleteEvent(event.source, event.target, event.upwards, true))
            event.target.consume(event)
        }
    }

}