package inventory.dropItem

import core.Player
import core.commands.Args
import core.commands.parseVector
import core.commands.respond
import core.events.EventManager
import core.history.displayToMe
import core.thing.Thing
import traveling.position.Vector

class DropItemCommand : core.commands.Command() {

    override fun getAliases(): List<String> {
        return listOf("Drop", "Place")
    }

    override fun getDescription(): String {
        return "Drop an item from your inventory onto the ground."
    }

    override fun getManual(): String {
        return """
	Drop <item> - Drop an item an item from your inventory.
	Place <item> at <vector> - Place the item at a specific place."""
    }

    override fun getCategory(): List<String> {
        return listOf("Inventory")
    }

    override fun execute(source: Player, keyword: String, args: List<String>) {
        val arguments = Args(args, delimiters = listOf("at"))
        val vector = parseVector(args) ?: source.position
        when {
            arguments.isEmpty() -> clarifyItemToDrop(source)
            arguments.hasBase() -> dropItem(source.thing, arguments, vector)
            else -> source.displayToMe("Drop what? Try 'drop <item>'.")
        }
    }

    private fun clarifyItemToDrop(source: Player) {
        source.respond("You have nothing you can drop.") {
            message("Drop what item?")
            options(source.inventory.getItems())
            command { "drop $it" }
        }
    }

    private fun dropItem(source: Thing, args: Args, position: Vector) {
        val item = source.inventory.getItem(args.getBaseString())
        if (item != null) {
            EventManager.postEvent(PlaceItemEvent(source, item, position))
        } else {
            source.displayToMe("Couldn't find ${args.getBaseString()}")
        }
    }




}