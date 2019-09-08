package combat.approach

import combat.battle.Distances
import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.history.display
import system.EventManager

class ApproachCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Approach", "Forward", "Advance")
    }

    override fun getDescription(): String {
        return "Approach:\n\tMove closer to the enemy."
    }

    override fun getManual(): String {
        return "\n\tApproach - Move closer to the enemy (only works in battle)"
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }


    override fun execute(keyword: String, args: List<String>) {
        val amount = Args(args).getNumber()
        when {
            GameState.battle == null -> display("This is only relevant in battle.")
            GameState.battle?.getCombatantDistance() ?: 0 <= 1 -> display("You can't get any closer.")
            keyword.toLowerCase() == "approach" && args.isEmpty() -> clarifyAmount()
            amount == null -> approach(getDefaultAmount())
            else -> approach(amount)
        }
    }

    private fun getDefaultAmount(): Int {
        return GameState.player.position.getDistance(GameState.battle!!.getOpponent(GameState.player)!!.target.position)
    }

    private fun clarifyAmount() {
        val targetRange = getDefaultAmount()
        val targets = listOf(Distances.MIN_RANGE, targetRange/2, targetRange)
        display("Move how much?")
        CommandParser.responseRequest = ResponseRequest(targets.map { "$it" to "approach $it" }.toMap())
    }

    private fun approach(amount: Int) {
        val target = GameState.player.position.getVectorInDirection(GameState.battle!!.getOpponent(GameState.player)!!.target.position, amount)
        EventManager.postEvent(StartMoveEvent(GameState.player, target))
    }

}