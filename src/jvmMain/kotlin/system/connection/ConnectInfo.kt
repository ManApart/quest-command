package system.connection

import core.commands.CommandParsers
import core.events.EventListener
import core.history.display
import core.history.displayToMe
import java.awt.SystemColor.info

actual class ConnectInfo : EventListener<ConnectInfoEvent>() {
    actual override fun execute(event: ConnectInfoEvent) {
        val info = WebClient.getInfo()
            when {
                CommandParsers.getParser(event.source).commandInterceptor !is ConnectionCommandInterceptor -> event.source.displayToMe("You're not connected to a server. You're targeting $info.")
                WebClient.connected -> event.source.displayToMe(info)
                else -> event.source.displayToMe("${WebClient.host}:${WebClient.port} is not a valid server.")
        }
    }
}