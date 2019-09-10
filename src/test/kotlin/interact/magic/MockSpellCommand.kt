package interact.magic

import combat.battle.position.TargetAim
import core.commands.Args
import core.gameState.Target
import interact.magic.spellCommands.SpellCommand

class MockSpellCommand(override val name: String = "testspell", private val category: List<String> = listOf()) : SpellCommand() {
    var args = Args(listOf())
    var targets = listOf<TargetAim>()

    override fun getDescription(): String {
        return ""
    }

    override fun getManual(): String {
        return ""
    }

    override fun getCategory(): List<String> {
        return category
    }

    override fun execute(args: Args, targets: List<TargetAim>) {
        this.args = args
        this.targets = targets
    }


}