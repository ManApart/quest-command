package system.connection

import core.Player
import core.events.Event

class ConnectEvent(val source: Player, val host: String = "localhost", val port: String = "8080") : Event