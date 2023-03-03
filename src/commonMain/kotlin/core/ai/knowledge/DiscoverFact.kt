package core.ai.knowledge

import core.events.EventListener

class DiscoverFact : EventListener<DiscoverFactEvent>() {

    override suspend fun complete(event: DiscoverFactEvent) {
        event.source.mind.learn(event.fact)
    }
}