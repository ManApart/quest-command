package core.gameState.quests

import core.events.Event

class QuestStageUpdatedEvent(val quest: Quest, val stage: Int) : Event