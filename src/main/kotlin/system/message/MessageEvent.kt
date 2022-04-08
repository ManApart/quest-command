package system.message

import core.Player
import core.events.Event

class MessageEvent(val source: Player, val message: String) : Event

fun messageEvent(source: Player?, message: String): MessageEvent? {
    return source?.let { MessageEvent(source, message) }
}