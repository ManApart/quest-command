package system.connection

import core.GameState
import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe
import system.debug.DebugType

class Connect : EventListener<ConnectEvent>() {

    override fun execute(event: ConnectEvent) {
        WebClient.createServerConnectionIfPossible(event.host, event.port, event.playerName) { info ->
            if (info.validServer) {
                CommandParsers.getParser(event.source).commandInterceptor = ConnectionCommandInterceptor()
                event.source.displayToMe("Connected. Server info: $info")
                WebClient.getServerHistory { updates ->
                    updates.takeLast(10).forEach { event.source.displayToMe(it) }
                }
                if (GameState.getDebugBoolean(DebugType.POLL_CONNECTION)) WebClient.pollForUpdates()
            } else {
                event.source.displayToMe("Could not connect to ${event.playerName} on ${event.host}:${event.port}")
            }
        }
    }
}