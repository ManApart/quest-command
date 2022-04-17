package system.connection

import core.events.EventListener
import core.history.display

class ConnectInfo : EventListener<ConnectInfoEvent>() {
    override fun execute(event: ConnectInfoEvent) {
        event.source.display(WebClient.getServerInfo())
    }
}