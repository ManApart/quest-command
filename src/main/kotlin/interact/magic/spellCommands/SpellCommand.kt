package interact.magic.spellCommands

import combat.battle.position.TargetAim
import core.commands.Args
import core.commands.CommandParser
import core.commands.ResponseRequest
import core.gameState.GameState
import core.gameState.stat.FOCUS
import core.history.display
import core.utility.Named
import core.utility.StringFormatter

abstract class SpellCommand : Named {
    abstract fun getDescription(): String
    abstract fun getManual(): String
    abstract fun getCategory(): List<String>
    abstract fun execute(args: Args, targets: List<TargetAim>)

    fun executeWithWarns(levelStat: String, levelRequirement: Int, totalCost: Int, targets: List<TargetAim>, minTargetCount: Int = 1, maxTargetCount: Int = 100, execute: () -> Unit) {
        val level = GameState.player.soul.getCurrent(levelStat)
        val focus = GameState.player.soul.getCurrent(FOCUS)
        when {
            targets.size < minTargetCount ->
                display("Not enough targets were selected for this word.")
            targets.size > maxTargetCount ->
                display("Too many targets were selected for this word.")
            level < levelRequirement ->
                display("You are too low level to speak this word with this amount of force. ($level/$levelRequirement)")
            focus < totalCost ->
                display("You do not have enough focus to speak this word with this amount of force. ($focus/$totalCost)")
            else -> execute()
        }
    }

}