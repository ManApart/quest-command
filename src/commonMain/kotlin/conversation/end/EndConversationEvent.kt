package conversation.end

import conversation.Conversation
import core.Player
import core.events.Event

class EndConversationEvent(val source: Player, val conversation: Conversation) : Event