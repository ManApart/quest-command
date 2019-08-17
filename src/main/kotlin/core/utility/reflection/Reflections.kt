package core.utility.reflection

import core.commands.Command
import core.events.EventListener
import interact.magic.spellCommands.SpellCommand

interface Reflections {
    fun getCommands(): List<Command>
    fun getSpellCommands(): List<SpellCommand>
    fun getEventListeners(): List<EventListener<*>>
}