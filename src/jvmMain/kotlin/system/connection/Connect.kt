package system.connection

import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe
import java.awt.SystemColor.info

actual class Connect : EventListener<ConnectEvent>() {

    actual override fun execute(event: ConnectEvent) {
        WebClient.connectToServer(event.host, event.port, event.playerName)
        CommandParsers.getParser(event.source).commandInterceptor = ConnectionCommandInterceptor()
    }
}