package system.connection

import core.events.EventListener
import core.history.display

class Connect : EventListener<ConnectEvent>() {

    override fun execute(event: ConnectEvent) {
        if (!WebClient.isValidServer(event.host, event.port)) {
            event.source.display("Could not connect to ${event.host}:${event.port}")
        } else {
            WebClient.host = event.host
            WebClient.port = event.port
//        CommandParsers.getParser(event.source).commandInterceptor  = ConversationCommandInterceptor(Conversation(event.source.thing, event.listener))
            event.source.display("Connected. Server info: ${WebClient.getServerInfo()}")
        }
    }
}