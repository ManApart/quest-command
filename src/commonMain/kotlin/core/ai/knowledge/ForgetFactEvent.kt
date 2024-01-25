package core.ai.knowledge

import core.events.Event
import core.thing.Thing

data class ForgetFactEvent(val source: Thing, val fact: Fact? = null, val listFact: ListFact? = null, val kind: String? = null) : Event {
    init {
        require(fact != null || listFact != null || kind != null)
    }
}
