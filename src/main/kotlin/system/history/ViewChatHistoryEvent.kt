package system.history

import core.events.Event
import core.target.Target

class ViewChatHistoryEvent(val source: Target, val numberOfLinesToShow: Int = 10, val viewResponses: Boolean = false) : Event