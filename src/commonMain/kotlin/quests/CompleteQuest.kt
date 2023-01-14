package quests

import core.events.EventListener

class CompleteQuest : EventListener<CompleteQuestEvent>() {

    override suspend fun execute(event: CompleteQuestEvent) {
        event.quest.completeQuest()
    }


}