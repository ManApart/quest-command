package interact.magic.spellCommands.water

import core.gameState.Target
import interact.magic.spellCommands.SpellCommand

class Jet : SpellCommand {
    override val name = "Jet"

    override fun getDescription(): String {
        return "Cast Jet:\n\tBurst of water that does one time damage to one or more targets."
    }

    override fun getManual(): String {
        return "\n\tCast Jet <damage amount> on *<body part> of *<targets> - Burst of water that does one time damage to one or more targets."
    }

    override fun getCategory(): List<String> {
        return listOf("Water")
    }

    override fun execute(args: List<String>, targets: List<Target>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}