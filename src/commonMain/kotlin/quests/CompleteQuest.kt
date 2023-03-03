package quests

import core.events.EventListener

class CompleteQuest : EventListener<CompleteQuestEvent>() {

    override suspend fun complete(event: CompleteQuestEvent) {
        event.quest.completeQuest()
    }


}