package quests.journal

import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
import core.target.Target
import quests.QuestManager

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

    override fun execute(source: Target, keyword: String, args: List<String>) {
        when {
            args.isEmpty() && keyword == "Quest" -> clarifyQuest()

            args.isEmpty() -> EventManager.postEvent(ViewQuestListEvent(source, justActive = true))
            args.size == 1 && args[0] == "active" -> EventManager.postEvent(ViewQuestListEvent(source, justActive = true))

            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ViewQuestListEvent(source, justActive = false))

            args.size == 1 && args[0] == "quest" -> clarifyWhichQuest()

            else -> {
                val quest = QuestManager.quests.getOrNull(args.joinToString())
                if (quest != null) {
                    EventManager.postEvent(ViewQuestJournalEvent(source, quest))
                } else {
                    source.displayToMe("Couldn't find quest: ${args.joinToString(" ")}")
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


