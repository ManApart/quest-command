package system.connection

import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe

class Disconnect : EventListener<ConnectEvent>() {

    override fun execute(event: ConnectEvent) {
        WebClient.doPolling = false
        CommandParsers.getParser(event.source).commandInterceptor = null
        event.source.displayToMe("Disconnected.")
    }
}