package system.connection

import core.events.EventListener

expect class Connect : EventListener<ConnectEvent> {
    override fun execute(event: ConnectEvent)
}