package magic.spellCommands

import core.GameState
import core.commands.Args
import core.history.displayToMe
import core.thing.Thing
import core.utility.Named
import status.stat.FOCUS
import system.debug.DebugType
import traveling.position.ThingAim

abstract class SpellCommand : Named {
    abstract fun getDescription(): String
    abstract fun getManual(): String
    abstract fun getCategory(): List<String>
    abstract fun execute(source: Thing, args: Args, things: List<ThingAim>, useDefaults: Boolean)

    fun executeWithWarns(source: Thing, levelStat: String, levelRequirement: Int, totalCost: Int, things: List<ThingAim>, minThingCount: Int = 1, maxThingCount: Int = 100, execute: () -> Unit) {
        val level = source.soul.getCurrent(levelStat)
        val focus = source.soul.getCurrent(FOCUS)
        when {
            things.size < minThingCount ->
                source.displayToMe("Not enough things were selected for this word.")
            things.size > maxThingCount ->
                source.displayToMe("Too many things were selected for this word.")
            level < levelRequirement && !GameState.getDebugBoolean(DebugType.LEVEL_REQ) ->
                source.displayToMe("You are too low level to speak this word with this amount of force. ($levelStat: $level/$levelRequirement)")
            focus < totalCost && !GameState.getDebugBoolean(DebugType.STAT_CHANGES) ->
                source.displayToMe("You do not have enough focus to speak this word with this amount of force. ($focus/$totalCost)")
            else -> execute()
        }
    }

}