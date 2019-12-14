package use.interaction.nothing

import core.events.EventListener


class DoNothing : EventListener<NothingEvent>() {
    override fun execute(event: NothingEvent) {
        println("${event.source} does nothing.")
    }


}