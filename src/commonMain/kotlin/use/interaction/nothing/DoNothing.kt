package use.interaction.nothing

import core.events.EventListener
import core.history.display
import core.utility.asSubject
import core.utility.ifYouWord

class DoNothing : EventListener<NothingEvent>() {
    override fun execute(event: NothingEvent) {
        with(event) {
            source.display { "${source.asSubject(it)} ${source.ifYouWord(it, "do", "does")} nothing." }
        }
    }
}