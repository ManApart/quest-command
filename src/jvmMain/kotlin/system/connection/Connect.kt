package system.connection

import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe
import system.debug.DebugType

actual class Connect : EventListener<ConnectEvent>() {

    actual override suspend fun complete(event: ConnectEvent) {
        val info = WebClient.createServerConnectionIfPossible(event.host, event.port, event.playerName)
        if (info.validServer) {
            CommandParsers.getParser(event.source).commandInterceptor = ConnectionCommandInterceptor()
            event.source.displayToMe("Connected. Server info: $info")
            WebClient.getServerHistory().takeLast(10).forEach { event.source.displayToMe(it) }
            if (GameState.getDebugBoolean(DebugType.POLL_CONNECTION)) WebClient.pollForUpdates()
        } else {
            event.source.displayToMe("Could not connect to ${event.playerName} on ${event.host}:${event.port}")
        }
    }
}