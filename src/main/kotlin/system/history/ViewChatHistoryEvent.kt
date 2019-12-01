package system.history

import core.events.Event

class ViewChatHistoryEvent(val numberOfLinesToShow: Int = 10, val viewResponses: Boolean = false) : Event