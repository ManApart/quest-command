package combat.block

import combat.HandHelper
import core.Player
import core.commands.Args
import core.commands.Command
import core.commands.parseBodyParts
import core.events.EventManager
import core.thing.Thing

class BlockCommand : Command() {
    override fun getAliases(): List<String> {
        return listOf("Block")
    }

    override fun getDescription(): String {
        return "Attempt to block a blow."
    }

    override fun getManual(): String {
        return """
	Block <part> with <hand> - Attempt to block a direction with the item in your left/right hand. (Only works in battle).
	You stop blocking next time you choose an action."""
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override suspend fun suggest(source: Player, keyword: String, args: List<String>): List<String> {
        return when {
            args.isEmpty() -> source.body.getParts().map { it.name }
            args.last() == "with" -> listOf("right", "left") + source.body.getEquippedItems().map { it.name }
            else -> listOf("with")
        }
    }

    override suspend fun execute(source: Thing, keyword: String, args: List<String>) {
        val arguments = Args(args, listOf("with"))
        val handHelper = HandHelper(source, arguments.getString("with"), "block")
        val shieldedPart = parseBodyParts(source, arguments.getBaseGroup()).firstOrNull() ?: handHelper.hand
        EventManager.postEvent(StartBlockEvent(source, handHelper.hand, shieldedPart))
    }

}