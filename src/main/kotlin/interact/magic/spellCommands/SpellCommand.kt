package interact.magic.spellCommands

import core.gameState.Target
import core.utility.Named

interface SpellCommand : Named{
    fun getDescription(): String
    fun getManual(): String
    fun getCategory(): List<String>
    fun execute(args: List<String>, targets: List<Target>)
}