package system.connection

import core.events.EventListener

expect class Connect() : EventListener<ConnectEvent> {
    override suspend fun complete(event: ConnectEvent)
}