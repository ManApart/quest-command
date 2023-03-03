package system.connection

import addHistoryMessageAfterCallback
import core.commands.CommandParsers
import core.events.EventListener

actual class ConnectInfo : EventListener<ConnectInfoEvent>() {
    actual override suspend fun complete(event: ConnectInfoEvent) {
        val info = WebClient.getServerInfo()
        when {
            CommandParsers.getParser(event.source).commandInterceptor !is ConnectionCommandInterceptor -> addHistoryMessageAfterCallback("You're not connected to a server. You're targeting $info.")
            info.validServer -> addHistoryMessageAfterCallback(info.toString())
            else -> addHistoryMessageAfterCallback("${WebClient.host}:${WebClient.port} is not a valid server.")
        }
    }
}