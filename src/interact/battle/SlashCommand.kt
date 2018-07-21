package interact.battle

import core.commands.Command

class SlashCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Slash")
    }

    override fun getDescription(): String {
        return "Slash:\n\tSlash the target"
    }

    override fun getManual(): String {
        return "\n\tSlash <target> - Slash the target with the item in your right hand" +
                "\n\tSlash <target> *left - Slash the target with the item in your left hand" +
                "\n\tSlashing a target damages it based on the slash damage of the item you're holding in that hand, or the slap damage you do if empty handed"
    }

    override fun getCategory(): List<String> {
        return listOf("Battle")
    }

    override fun execute(keyword: String, args: List<String>) {
        val argsString = args.joinToString(" ")

    }
}