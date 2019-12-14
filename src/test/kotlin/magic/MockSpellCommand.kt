package magic

import combat.battle.position.TargetAim
import core.commands.Args
import core.target.Target
import magic.spellCommands.SpellCommand

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

    override fun execute(source: Target, args: Args, targets: List<TargetAim>, useDefaults: Boolean) {
        this.args = args
        this.targets = targets
    }


}