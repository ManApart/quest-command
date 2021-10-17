package magic

import core.commands.Args
import core.thing.Thing
import magic.spellCommands.SpellCommand
import traveling.position.ThingAim

class SpellCommandMock(override val name: String = "testspell", private val category: List<String> = listOf()) : SpellCommand() {
    var args = Args(listOf())
    var things = listOf<ThingAim>()

    override fun getDescription(): String {
        return ""
    }

    override fun getManual(): String {
        return ""
    }

    override fun getCategory(): List<String> {
        return category
    }

    override fun execute(source: Thing, args: Args, things: List<ThingAim>, useDefaults: Boolean) {
        this.args = args
        this.things = things
    }


}