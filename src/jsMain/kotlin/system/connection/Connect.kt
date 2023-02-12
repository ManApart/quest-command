package system.connection

import addHistoryMessageAfterCallback
import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import system.debug.DebugType

actual class Connect : EventListener<ConnectEvent>() {
    actual override suspend fun execute(event: ConnectEvent) {
        val info = WebClient.createServerConnectionIfPossible(event.host, event.port, event.playerName)
        if (info.validServer) {
            CommandParsers.getParser(event.source).commandInterceptor = ConnectionCommandInterceptor()
            addHistoryMessageAfterCallback("Connected. Server info: $info")
            WebClient.getServerUpdates().takeLast(10).forEach { addHistoryMessageAfterCallback(it) }
            if (GameState.getDebugBoolean(DebugType.POLL_CONNECTION)) WebClient.pollForUpdates()
        } else {
            addHistoryMessageAfterCallback("Could not connect to ${event.playerName} on ${event.host}:${event.port}")
        }
    }
}