package quests

import core.events.EventListener

class CompleteQuest : EventListener<CompleteQuestEvent>() {

    override fun execute(event: CompleteQuestEvent) {
        event.quest.completeQuest()
    }


}