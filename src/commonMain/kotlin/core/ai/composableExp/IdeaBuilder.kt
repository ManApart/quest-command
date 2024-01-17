package core.ai.composableExp

import core.events.Event
import core.thing.Thing

class IdeaBuilder(val name: String) {
    private var criteria: suspend (Thing) -> Boolean = { true }
    private var action: suspend (Thing) -> List<Event> = { listOf() }


    fun build() = Idea(name, criteria, action)

    fun criteria(criteria: suspend (Thing) -> Boolean) {
        this.criteria = criteria
    }

    fun actions(action: suspend (Thing) -> List<Event>) {
        this.action = action
    }

    fun action(action: suspend (Thing) -> Event) {
        this.action = { listOf(action(it)) }
    }

}

