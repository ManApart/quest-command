package conversation.input

import conversation.Conversation
import core.events.Event

class Dialogue(val result: (Conversation) -> List<Event>, val conditions: List<(Conversation) -> Boolean>)