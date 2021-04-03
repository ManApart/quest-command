package inventory

import core.commands.Command
import core.GameState
import core.history.display

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

    override fun execute(keyword: String, args: List<String>) {
        if (args.isEmpty()) {
            listEquipped()
        } else {
            display("Unknown command: equip ${args.joinToString(" ")}")
        }
    }

    private fun listEquipped() {
        val body = GameState.player.body
        val items = body.getEquippedItems()
        if (items.isEmpty()) {
            display("You don't have anything equipped!")
        } else {
            val itemList = items.joinToString("\n\t") { "${it.name} equipped to ${it.getEquippedSlot(body).description}" }
            display("You have following items equipped:\n\t$itemList")
        }
    }

}