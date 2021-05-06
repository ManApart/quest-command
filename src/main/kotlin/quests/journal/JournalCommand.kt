package quests.journal

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import quests.QuestManager
import core.history.display
import core.events.EventManager

class JournalCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Quest", "Quests", "Journal", "Q")
    }

    override fun getDescription(): String {
        return "View your active and completed Quests."
    }

    override fun getManual(): String {
        return """
	Quest active - View active Quests.
	Quest all - View all quests.
	Quest <quest> - View entries for a specific quest."""
    }

    override fun getCategory(): List<String> {
        return listOf("Character")
    }

    override fun execute(keyword: String, args: List<String>) {
        when {
            args.isEmpty() && keyword == "Quest" -> clarifyQuest()

            args.isEmpty() -> EventManager.postEvent(ViewQuestListEvent(justActive = true))
            args.size == 1 && args[0] == "active" -> EventManager.postEvent(ViewQuestListEvent(justActive = true))

            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ViewQuestListEvent(justActive = false))

            args.size == 1 && args[0] == "quest" -> clarifyWhichQuest()

            else -> {
                val quest = QuestManager.quests.getOrNull(args.joinToString())
                if (quest != null) {
                    EventManager.postEvent(ViewQuestJournalEvent(quest))
                } else {
                    display("Couldn't find quest: ${args.joinToString(" ")}")
                }
            }
        }
    }

    private fun clarifyQuest() {
        val targets = listOf("Active", "All", "Quest")
        val message = "Info about what type?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest( ResponseRequest(message, targets.associateWith { "quest $it" }))
    }

    private fun clarifyWhichQuest() {
        val targets = QuestManager.getAllPlayerQuests().map { it.name }
        val message = "Info about which quest?\n\t${targets.joinToString(", ")}"
        CommandParser.setResponseRequest( ResponseRequest(message, targets.associateWith { "quest $it" }))
    }

}


