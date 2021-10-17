package conversation.start

import core.events.Event
import core.thing.Thing

class StartConversationEvent(val speaker: Thing, val listener: Thing) : Event