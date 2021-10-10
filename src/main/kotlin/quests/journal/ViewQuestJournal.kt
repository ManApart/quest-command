package quests.journal

import core.events.EventListener
import core.history.displayToMe

class ViewQuestJournal : EventListener<ViewQuestJournalEvent>() {

    override fun execute(event: ViewQuestJournalEvent) {
        val message = "\t" + event.quest.getAllJournalEntries().joinToString("\n\t")
        event.source.displayToMe(message)
    }

}