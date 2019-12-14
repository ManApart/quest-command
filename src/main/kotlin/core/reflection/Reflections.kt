package core.reflection

import core.commands.Command
import core.events.EventListener
import core.events.eventParsers.EventParser
import magic.spellCommands.SpellCommand

interface Reflections {
    fun getCommands(): List<Command>
    fun getSpellCommands(): List<SpellCommand>
    fun getEventParsers(): List<EventParser>
    fun getEventListeners(): List<EventListener<*>>
}