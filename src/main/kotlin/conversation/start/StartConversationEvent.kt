package conversation.start

import core.events.Event
import core.target.Target

class StartConversationEvent(val speaker: Target, val listener: Target) : Event