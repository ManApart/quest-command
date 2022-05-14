package system.connection

import core.commands.CommandParsers
import core.events.EventListener
import core.history.display
import core.history.displayToMe

class ConnectInfo : EventListener<ConnectInfoEvent>() {
    override fun execute(event: ConnectInfoEvent) {
        val info = WebClient.getServerInfo()
        when {
            CommandParsers.getParser(event.source).commandInterceptor !is ConnectionCommandInterceptor -> event.source.displayToMe("You're not connected to a server. You're targeting $info.")
            info.validServer -> event.source.displayToMe(info.toString())
            else -> event.source.displayToMe("${WebClient.host}:${WebClient.port} is not a valid server.")
        }
    }
}