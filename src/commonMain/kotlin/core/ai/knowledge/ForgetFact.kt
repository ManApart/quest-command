package core.ai.knowledge

import core.events.EventListener

class ForgetFact : EventListener<ForgetFactEvent>() {

    override suspend fun complete(event: ForgetFactEvent) {
        with(event.source.mind.memory) {
            when {
                event.fact != null -> forget(event.fact)
                event.listFact != null -> forget(event.listFact)
                event.kind != null -> forget(event.kind)
            }
        }

    }
}