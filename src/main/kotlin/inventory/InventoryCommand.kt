package inventory

import core.commands.Command
import core.gameState.getCreature
import core.history.display
import interact.scope.ScopeManager
import system.EventManager

class InventoryCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Bag", "b", "backpack")
    }

    override fun getDescription(): String {
        return "Bag:\n\tView and manage your inventory."
    }

    override fun getManual(): String {
        return "\n\tBag - list items in your inventory." +
                "\n\tBag <target> - list items in the target's inventory, if possible."
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (args.isEmpty()) {
            EventManager.postEvent(ListInventoryEvent())
        } else {
            val argString = args.joinToString(" ")
            val target = ScopeManager.getScope().getTarget(argString)?.getCreature()
            if (target != null) {
                EventManager.postEvent(ListInventoryEvent(target))
            } else {
                display("Could not find $argString")
            }
        }
    }


}