package system.message

import core.Player
import core.events.Event
import core.thing.Thing

data class MessageEvent(val source: Thing, val messageToYou: String, val messageToOthers: String = messageToYou, val private: Boolean = (messageToYou == messageToOthers)) : Event{
    constructor(source: Player, messageToYou: String, messageToOthers: String = messageToYou, private: Boolean = true) : this(source.thing, messageToYou, messageToOthers, private)
}
