package system.help

import core.events.EventListener
import core.history.displayToMe

class ViewVersion : EventListener<ViewVersionEvent>() {
    override suspend fun complete(event: ViewVersionEvent) {
        event.source.displayToMe("Version: $APP_VERSION")
    }
}