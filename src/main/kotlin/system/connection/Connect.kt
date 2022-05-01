package system.connection

import core.GameState
import core.POLL_CONNECTION
import core.commands.CommandParsers
import core.events.EventListener
import core.history.display
import core.history.displayToMe

class Connect : EventListener<ConnectEvent>() {

    override fun execute(event: ConnectEvent) {
        val info = WebClient.createServerConnectionIfPossible(event.host, event.port, event.playerName)
        if (info.validServer) {
            CommandParsers.getParser(event.source).commandInterceptor = ConnectionCommandInterceptor()
            event.source.displayToMe("Connected. Server info: $info")
            val updates = WebClient.getServerHistory()
            updates.takeLast(10).forEach { event.source.displayToMe(it) }
            if (GameState.properties.values.getBoolean(POLL_CONNECTION)) WebClient.pollForUpdates()
        } else {
            event.source.displayToMe("Could not connect to ${event.host}:${event.port}")
        }
    }
}