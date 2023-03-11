package use.interaction.nothing

import core.events.EventListener
import core.history.display
import core.utility.asSubject
import core.utility.ifYouWord

class DoNothing : EventListener<NothingEvent>() {
    override suspend fun complete(event: NothingEvent) {
        with(event) {
            creature.display { "${creature.asSubject(it)} ${creature.ifYouWord(it, "do", "does")} nothing." }
        }
    }
}