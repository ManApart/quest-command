package magic.spellCommands

import core.GameState
import core.commands.Args
import core.history.displayToMe
import core.target.Target
import core.utility.Named
import status.stat.FOCUS
import system.debug.DebugType
import traveling.position.TargetAim

abstract class SpellCommand : Named {
    abstract fun getDescription(): String
    abstract fun getManual(): String
    abstract fun getCategory(): List<String>
    abstract fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean)

    fun executeWithWarns(source: Target, levelStat: String, levelRequirement: Int, totalCost: Int, targets: List<TargetAim>, minTargetCount: Int = 1, maxTargetCount: Int = 100, execute: () -> Unit) {
        val level = source.soul.getCurrent(levelStat)
        val focus = source.soul.getCurrent(FOCUS)
        when {
            targets.size < minTargetCount ->
                source.displayToMe("Not enough targets were selected for this word.")
            targets.size > maxTargetCount ->
                source.displayToMe("Too many targets were selected for this word.")
            level < levelRequirement && !GameState.getDebugBoolean(DebugType.LEVEL_REQ) ->
                source.displayToMe("You are too low level to speak this word with this amount of force. ($levelStat: $level/$levelRequirement)")
            focus < totalCost && !GameState.getDebugBoolean(DebugType.STAT_CHANGES) ->
                source.displayToMe("You do not have enough focus to speak this word with this amount of force. ($focus/$totalCost)")
            else -> execute()
        }
    }

}