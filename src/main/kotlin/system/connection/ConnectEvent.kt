package system.connection

import core.Player
import core.events.Event

data class ConnectEvent(val source: Player, val playerName: String, val host: String = "localhost", val port: String = "8080") : Event