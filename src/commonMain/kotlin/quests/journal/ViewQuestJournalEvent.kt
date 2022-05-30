package quests.journal

import core.events.Event
import core.thing.Thing
import quests.Quest

class ViewQuestJournalEvent(val source: Thing, val quest: Quest) : Event