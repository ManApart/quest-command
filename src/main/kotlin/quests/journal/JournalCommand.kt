package quests.journal

import core.Player
import core.commands.Command
import core.commands.CommandParsers
import core.commands.ResponseRequest
import core.events.EventManager
import core.history.displayToMe
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

    override fun execute(source: Player, keyword: String, args: List<String>) {
        when {
            args.isEmpty() && keyword == "Quest" -> clarifyQuest(source)

            args.isEmpty() -> EventManager.postEvent(ViewQuestListEvent(source.thing, justActive = true))
            args.size == 1 && args[0] == "active" -> EventManager.postEvent(ViewQuestListEvent(source.thing, justActive = true))

            args.size == 1 && args[0] == "all" -> EventManager.postEvent(ViewQuestListEvent(source.thing, justActive = false))

            args.size == 1 && args[0] == "quest" -> clarifyWhichQuest(source)

            else -> {
                val quest = QuestManager.quests.getOrNull(args.joinToString())
                if (quest != null) {
                    EventManager.postEvent(ViewQuestJournalEvent(source.thing, quest))
                } else {
                    source.displayToMe("Couldn't find quest: ${args.joinToString(" ")}")
                }
            }
        }
    }

    private fun clarifyQuest(source: Player) {
        val things = listOf("Active", "All", "Quest")
        val message = "Info about what type?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source,  ResponseRequest(message, things.associateWith { "quest $it" }))
    }

    private fun clarifyWhichQuest(source: Player) {
        val things = QuestManager.getAllPlayerQuests().map { it.name }
        val message = "Info about which quest?\n\t${things.joinToString(", ")}"
        CommandParsers.setResponseRequest(source,  ResponseRequest(message, things.associateWith { "quest $it" }))
    }

}


