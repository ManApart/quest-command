package system.connection

import core.commands.CommandParsers
import core.events.EventListener
import core.history.display

class Connect : EventListener<ConnectEvent>() {

    override fun execute(event: ConnectEvent) {
        val info = WebClient.createServerConnectionIfPossible(event.host, event.port, event.playerName)
        if (info.validServer) {
            CommandParsers.getParser(event.source).commandInterceptor = ConnectionCommandInterceptor()
            event.source.display("Connected. Server info: $info")
        } else {
            event.source.display("Could not connect to ${event.host}:${event.port}")
        }
    }
}