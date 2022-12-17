package core.ai.knowledge

import core.events.EventListener

class DiscoverFact : EventListener<DiscoverFactEvent>() {

    override fun execute(event: DiscoverFactEvent) {
        event.source.mind.learn(event.fact)
    }
}