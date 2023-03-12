package time

import core.GameState
import core.events.EventListener
import core.history.displayToMe

class ViewTime : EventListener<ViewTimeEvent>() {

    override suspend fun complete(event: ViewTimeEvent) {
        event.source.displayToMe(GameState.timeManager.getTimeString())
    }

}