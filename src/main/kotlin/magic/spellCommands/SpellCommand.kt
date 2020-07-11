package magic.spellCommands

import traveling.position.TargetAim
import core.commands.Args
import core.GameState
import core.target.Target
import status.stat.FOCUS
import core.history.display
import core.utility.Named
import system.debug.DebugType

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
                display("Not enough targets were selected for this word.")
            targets.size > maxTargetCount ->
                display("Too many targets were selected for this word.")
            level < levelRequirement && !GameState.properties.values.getBoolean(DebugType.LEVEL_REQ.propertyName) ->
                display("You are too low level to speak this word with this amount of force. ($levelStat: $level/$levelRequirement)")
            focus < totalCost && !GameState.properties.values.getBoolean(DebugType.STAT_CHANGES.propertyName) ->
                display("You do not have enough focus to speak this word with this amount of force. ($focus/$totalCost)")
            else -> execute()
        }
    }

}