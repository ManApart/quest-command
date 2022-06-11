package conversation.start

import core.Player
import core.events.Event
import core.thing.Thing

class StartConversationEvent(val speaker: Player, val listener: Thing) : Event