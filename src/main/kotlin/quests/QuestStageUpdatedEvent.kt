package quests

import core.events.Event

class QuestStageUpdatedEvent(val quest: Quest, val stage: Int) : Event