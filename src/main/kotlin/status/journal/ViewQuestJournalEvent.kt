package status.journal

import core.events.Event
import core.gameState.quests.Quest

class ViewQuestJournalEvent(val quest: Quest) : Event