package system.history

import core.events.Event
import core.gameState.Target

class ViewChatHistoryEvent(val numberOfLinesToShow: Int = 10, val viewResponses: Boolean = false) : Event