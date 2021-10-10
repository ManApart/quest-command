package quests

import core.events.Event
import core.target.Target

class QuestStageUpdatedEvent(val source: Target, val quest: Quest, val stage: Int) : Event