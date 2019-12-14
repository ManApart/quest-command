package use.interaction.nothing

import core.events.EventListener
import core.GameState


class StartNothing : EventListener<StartNothingEvent>() {
    override fun execute(event: StartNothingEvent) {
        GameState.battle?.addAction(event.source, event)
    }
}