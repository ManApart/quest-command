package status.journal

import core.events.EventListener
import core.gameState.quests.Quest
import core.gameState.quests.QuestManager
import core.history.display
import core.utility.NameSearchableList

class ViewQuestJournal : EventListener<ViewJournalEvent>() {

    override fun execute(event: ViewJournalEvent) {
        val quests = getQuests(event)

        val message = quests.joinToString("\n") {
            "${it.name}\n\t${it.getLatestJournalEntry()}"
        }

        displayQuestMessage(message, event)
    }

    private fun getQuests(event: ViewJournalEvent): NameSearchableList<Quest> {
        return if (event.justActive) {
            QuestManager.getActiveQuests()
        } else {
            QuestManager.getAllPlayerQuests()
        }
    }

    private fun displayQuestMessage(message: String, event: ViewJournalEvent) {
        if (message.isBlank()) {
            if (event.justActive) {
                display("I don't have any active quests.")
            } else {
                display("I don't have any quests.")
            }
        } else {
            display(message)
        }
    }

}