package conversation.statement

import core.events.EventListener
import core.history.display


class StatementListener : EventListener<StatementEvent>() {
    override fun execute(event: StatementEvent) {
        display(event.print())
    }
}