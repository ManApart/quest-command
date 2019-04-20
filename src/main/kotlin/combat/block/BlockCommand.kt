package combat.block

import combat.HandHelper
import combat.battle.position.TargetDirection
import core.commands.Args
import core.commands.Command
import core.gameState.GameState
import core.history.display
import system.EventManager

class BlockCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Block")
    }

    override fun getDescription(): String {
        return "Block:\n\tAttempt to block a blow."
    }

    override fun getManual(): String {
        return "\n\tBlock <direction> with <hand> - Attempt to block a direction with the item in your left/right hand. (Only works in battle)." +
                "\n\tYou stop blocking next time you choose an action."
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        if (GameState.battle == null) {
            display("This is only relevant in battle.")
        } else {
            val arguments = Args(args, listOf("with"))
            val handHelper = HandHelper(arguments.getGroupString(1), "block")
            val direction =  TargetDirection.getTargetDirection(arguments.getGroupString(0)) ?: TargetDirection.getRandom()
            EventManager.postEvent(StartBlockEvent(GameState.player, handHelper.hand, direction))
        }
    }

}