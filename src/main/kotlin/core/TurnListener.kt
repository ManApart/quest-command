package core

import core.commands.CommandParser
import core.events.Event
import core.events.EventListener

class TurnListener : EventListener<Event>() {
    override fun execute(event: Event) {
        if (event.isExecutableByAI()) {
            CommandParser.commandSource = GameState.player
        }
    }
}