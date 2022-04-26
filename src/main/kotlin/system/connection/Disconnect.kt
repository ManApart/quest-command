package system.connection

import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe

class Disconnect : EventListener<DisconnectEvent>() {

    override fun execute(event: DisconnectEvent) {
        WebClient.doPolling = false
        CommandParsers.getParser(event.source).commandInterceptor = null
        event.source.displayToMe("Disconnected.")
    }
}