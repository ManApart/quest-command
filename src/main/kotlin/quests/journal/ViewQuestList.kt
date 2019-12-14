package quests.journal

import core.events.EventListener
import quests.Quest
import quests.QuestManager
import core.history.display
import core.utility.NameSearchableList

class ViewQuestList : EventListener<ViewQuestListEvent>() {

    override fun execute(event: ViewQuestListEvent) {
        val quests = getQuests(event)

        val message = quests.joinToString("\n") {
            "${it.name}\n\t${it.getLatestJournalEntry()}"
        }

        displayQuestMessage(message, event)
    }

    private fun getQuests(event: ViewQuestListEvent): NameSearchableList<Quest> {
        return if (event.justActive) {
            QuestManager.getActiveQuests()
        } else {
            QuestManager.getAllPlayerQuests()
        }
    }

    private fun displayQuestMessage(message: String, event: ViewQuestListEvent) {
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