package quests.journal

import core.events.Event
import core.target.Target
import quests.Quest

class ViewQuestJournalEvent(val source: Target, val quest: Quest) : Event