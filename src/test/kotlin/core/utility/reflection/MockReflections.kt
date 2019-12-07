package core.utility.reflection

import core.commands.Command
import core.events.EventListener
import core.gameState.dataParsing.events.EventParser
import interact.magic.spellCommands.SpellCommand

class MockReflections(private val commands: List<Command> = listOf(),
                      private val spellCommands: List<SpellCommand> = listOf(),
                      private val eventParsers: List<EventParser> = listOf(),
                      private val eventListeners: List<EventListener<*>> = listOf()
) : Reflections {

    override fun getCommands(): List<Command> {
        return commands
    }

    override fun getSpellCommands(): List<SpellCommand> {
        return spellCommands
    }

    override fun getEventParsers(): List<EventParser> {
        return eventParsers
    }

    override fun getEventListeners(): List<EventListener<*>> {
        return eventListeners
    }
}