package system.connection

import core.events.EventListener

expect class ConnectInfo() : EventListener<ConnectInfoEvent> {
    override fun execute(event: ConnectInfoEvent)
}