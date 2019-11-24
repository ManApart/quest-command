package system

import core.commands.CommandParser
import core.events.Event
import core.events.EventListener
import core.gameState.GameState
import system.EventManager
import system.message.DisplayMessageEvent

class TurnListener : EventListener<Event>() {
    override fun execute(event: Event) {
        if (event.isExecutableByAI()) {
            CommandParser.commandSource = GameState.player
        }
    }
}