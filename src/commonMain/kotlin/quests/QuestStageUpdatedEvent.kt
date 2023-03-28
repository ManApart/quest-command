package quests

import core.events.Event
import core.thing.Thing

data class QuestStageUpdatedEvent(val source: Thing, val quest: Quest, val stage: Int) : Event