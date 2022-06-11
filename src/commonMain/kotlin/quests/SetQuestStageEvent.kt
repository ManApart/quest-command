package quests

import core.events.Event

class SetQuestStageEvent(val quest: Quest, val stage: Int) : Event