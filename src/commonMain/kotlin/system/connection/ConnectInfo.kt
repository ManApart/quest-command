package system.connection

import core.events.EventListener

expect class ConnectInfo() : EventListener<ConnectInfoEvent> {
    override suspend fun execute(event: ConnectInfoEvent)
}