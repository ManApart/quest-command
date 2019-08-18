package interact.magic.spellCommands

import combat.battle.position.TargetAim
import core.commands.Args
import core.utility.Named

interface SpellCommand : Named{
    fun getDescription(): String
    fun getManual(): String
    fun getCategory(): List<String>
    fun execute(args: Args, targets: List<TargetAim>)
}