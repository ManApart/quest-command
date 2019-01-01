package status.journal

import core.events.EventListener
import core.gameState.stat.Stat
import core.history.display
import status.rest.RestEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class ViewQuestJournal : EventListener<ViewQuestJournalEvent>() {

    override fun execute(event: ViewQuestJournalEvent) {
        val message = "\t" + event.quest.getAllJournalEntries().joinToString("\n\t")
        display(message)
    }

}