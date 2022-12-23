package quests.journal

import core.events.EventListener
import core.history.displayToMe
import explore.listen.addSoundEffect

class ViewQuestJournal : EventListener<ViewQuestJournalEvent>() {

    override fun execute(event: ViewQuestJournalEvent) {
        val message = "\t" + event.quest.getAllJournalEntries().joinToString("\n\t")
        event.source.displayToMe(message)
        event.source.addSoundEffect("Reading", "the soft rustle of paper", 1)
    }

}