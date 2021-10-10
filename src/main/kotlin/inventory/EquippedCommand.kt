package inventory

import core.commands.Command
import core.history.displayToMe
import core.target.Target

class EquippedCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Equipped")
    }

    override fun getDescription(): String {
        return "View what you currently have equipped"
    }

    override fun getManual(): String {
        return """
	Equipped - View what you currently have equipped"""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(source: Target, keyword: String, args: List<String>) {
        if (args.isEmpty()) {
            listEquipped(source)
        } else {
            source.displayToMe("Unknown command: equip ${args.joinToString(" ")}")
        }
    }

    private fun listEquipped(source: Target) {
        val body = source.body
        val items = body.getEquippedItems()
        if (items.isEmpty()) {
            source.displayToMe("You don't have anything equipped!")
        } else {
            val itemList = items.joinToString("\n\t") { "${it.name} equipped to ${it.getEquippedSlot(body).description}" }
            source.displayToMe("You have following items equipped:\n\t$itemList")
        }
    }

}