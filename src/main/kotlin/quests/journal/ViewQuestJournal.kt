package quests.journal

import core.events.EventListener
import core.history.display

class ViewQuestJournal : EventListener<ViewQuestJournalEvent>() {

    override fun execute(event: ViewQuestJournalEvent) {
        val message = "\t" + event.quest.getAllJournalEntries().joinToString("\n\t")
        display(message)
    }

}