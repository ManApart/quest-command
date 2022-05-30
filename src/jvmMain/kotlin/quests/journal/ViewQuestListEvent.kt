package quests.journal

import core.events.Event
import core.thing.Thing

class ViewQuestListEvent(val source: Thing, val justActive: Boolean = false) : Event