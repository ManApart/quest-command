package system.connection

import core.commands.CommandParsers
import core.events.EventListener
import core.history.displayToMe

actual class Disconnect : EventListener<DisconnectEvent>() {

    actual override fun execute(event: DisconnectEvent) {
        if (CommandParsers.getParser(event.source).commandInterceptor is ConnectionCommandInterceptor) {
            WebClient.doPolling = false
            WebClient.latestResponse = 0
            CommandParsers.getParser(event.source).commandInterceptor = null
            event.source.displayToMe("Disconnected.")
        } else {
            event.source.displayToMe("You're not connected to a server.")
        }
    }
}