package system.connection

import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe
import system.debug.DebugType

class Connect : EventListener<ConnectEvent>() {

    override fun execute(event: ConnectEvent) {
        val info = WebClient.createServerConnectionIfPossible(event.host, event.port, event.playerName)
        if (info.validServer) {
            CommandParsers.getParser(event.source).commandInterceptor = ConnectionCommandInterceptor()
            event.source.displayToMe("Connected. Server info: $info")
            val updates = WebClient.getServerHistory()
            updates.takeLast(10).forEach { event.source.displayToMe(it) }
            if (GameState.getDebugBoolean(DebugType.POLL_CONNECTION)) WebClient.pollForUpdates()
        } else {
            event.source.displayToMe("Could not connect to ${event.host}:${event.port}")
        }
    }
}