package status.journal

import core.events.EventListener
import core.gameState.GameState
import core.gameState.dataParsing.GamestateQuery
import core.gameState.quests.QuestManager
import core.gameState.stat.Stat
import core.history.display
import status.rest.RestEvent
import status.statChanged.StatChangeEvent
import system.EventManager

class ViewQuestJournal : EventListener<ViewJournalEvent>() {

    override fun execute(event: ViewJournalEvent) {
        val message = QuestManager.getActiveQuests().joinToString("\n") {
            "${it.name}\n\t${it.getLatestJournalEntry()}"
        }

        if (message.isBlank()) {
            display("I don't have any active quests")
        } else {
            display(message)
        }
    }
}