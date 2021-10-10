package quests.journal

import core.events.Event
import core.target.Target

class ViewQuestListEvent(val source: Target, val justActive: Boolean = false) : Event