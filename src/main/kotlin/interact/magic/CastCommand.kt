package interact.magic

import core.commands.Command
import core.history.display
import core.utility.reflection.spellCommands
import system.EventManager

class CastCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Cast", "word")
    }

    override fun getDescription(): String {
        return "Cast:\n\tSpeak a word of power."
    }

    override fun getManual(): String {
        return "\n\tCast <word> on *<body part> of *<targets>. " +
                "word list - list known words." +
                "word <word> - view the manual for that word."
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        when (keyword) {
            "word" -> executeWord(args)
            else -> castWord(args)
        }
    }

    private fun executeWord(args: List<String>) {
        if (args.size <= 1) {
            if (args.isEmpty() || args.first() == "list") {
                EventManager.postEvent(ViewWordHelpEvent())
            } else if (isCategory(args)){
                EventManager.postEvent(ViewWordHelpEvent(args.first(), true))
            } else {
                EventManager.postEvent(ViewWordHelpEvent(args.first()))
            }
        } else {
            display("Unknown command: ${args.joinToString(" ")}")
        }
    }

    private fun isCategory(args: List<String>): Boolean {
        return spellCommands.asSequence().map { command -> command.getCategory().map { category -> category.toLowerCase() } }.contains(args)
    }

    private fun castWord(args: List<String>) {

        //parse target(s)
        // parse spell keyword
        //pass targets and additional args to proper spell command
    }
}