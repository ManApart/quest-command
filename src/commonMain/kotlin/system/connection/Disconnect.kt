package system.connection

import core.events.EventListener

expect class Disconnect : EventListener<DisconnectEvent> {
    override fun execute(event: DisconnectEvent)
}