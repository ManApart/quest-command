package system.connection

import core.events.EventListener

expect class Disconnect() : EventListener<DisconnectEvent> {
    override suspend fun execute(event: DisconnectEvent)
}