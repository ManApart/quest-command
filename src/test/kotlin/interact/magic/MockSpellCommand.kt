package interact.magic

import core.gameState.Target
import interact.magic.spellCommands.SpellCommand

class MockSpellCommand(override val name: String = "testspell", private val category: List<String> = listOf()) : SpellCommand {
    var args = listOf<String>()
    var targets = listOf<Target>()

    override fun getDescription(): String {
        return ""
    }

    override fun getManual(): String {
        return ""
    }

    override fun getCategory(): List<String> {
        return category
    }

    override fun execute(args: List<String>, targets: List<Target>) {
        this.args = args
        this.targets = targets
    }


}