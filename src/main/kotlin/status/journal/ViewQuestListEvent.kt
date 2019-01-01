package status.journal

import core.events.Event
import core.gameState.Creature

class ViewQuestListEvent(val justActive: Boolean = false) : Event