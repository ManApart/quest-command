package combat.approach

import combat.battle.Distances
import combat.battle.Distances.HUMAN_LENGTH
import combat.battle.Distances.LOCATION_SIZE
import core.commands.Args
import core.commands.Command
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.GameState
import core.history.display
import core.events.EventManager

class RetreatCommand : Command() {
    override fun getAliases(): Array<String> {
        return arrayOf("Retreat", "Backward", "Back")
    }

    override fun getDescription(): String {
        return "Retreat:\n\tMove further from the enemy."
    }

    override fun getManual(): String {
        return "\n\tRetreat - Move further from the enemy (only works in battle)."
    }

    override fun getCategory(): List<String> {
        return listOf("Combat")
    }

    override fun execute(keyword: String, args: List<String>) {
        val amount = Args(args).getNumber()
        when {
            GameState.battle == null -> display("This is only relevant in battle.")
            GameState.battle?.getCombatantDistance() ?: 0 >= LOCATION_SIZE -> display("You can't get any further.")
            keyword.toLowerCase() == "retreat" && args.isEmpty() -> clarifyAmount()
            amount == null -> retreat(HUMAN_LENGTH)
            else -> retreat(amount)
        }
    }

    private fun clarifyAmount() {
        val targets = listOf(Distances.MIN_RANGE, HUMAN_LENGTH / 2, HUMAN_LENGTH)
        CommandParser.setResponseRequest(ResponseRequest("Move how much?", targets.map { "$it" to "retreat $it" }.toMap()))
    }

    private fun retreat(amount: Int) {
        val opponent = GameState.battle!!.getOpponent(GameState.player)!!.target
        val oppositeOfOpponent = GameState.player.position.getInverse(opponent.position)
        val target = GameState.player.position.getVectorInDirection(oppositeOfOpponent, amount)
        EventManager.postEvent(StartMoveEvent(GameState.player, target))
    }


}