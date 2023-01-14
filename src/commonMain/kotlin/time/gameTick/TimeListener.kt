package time.gameTick

import core.GameState
import core.events.EventListener

class TimeListener : EventListener<GameTickEvent>() {

    override suspend fun execute(event: GameTickEvent) {
        GameState.timeManager.passTime(event.time)
    }

}