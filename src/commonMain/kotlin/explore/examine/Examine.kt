package explore.examine

import core.events.EventListener
import core.properties.IS_CLIMBING
import explore.look.*

class Examine : EventListener<ExamineEvent>() {

    override suspend fun complete(event: ExamineEvent) {
        when {
            event.source.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney(event.source, true)
            event.body != null && event.thing != null -> describePerceived(event.source, event.thing) { describeBodyDetailed(event.source, event.thing) }
            event.mind != null && event.thing != null -> describePerceived(event.source, event.thing) { describeMind(event.source, event.thing, event.mind) }
            event.thing != null -> describePerceived(event.source, event.thing) { describeThingDetailed(event.source, event.thing) }
            event.location != null -> describeLocationDetailed(event.source, event.location)
            event.source.mind.getAggroTarget() != null -> describeBattle(event.source)
            else -> describeLocationDetailed(event.source, event.source.thing.currentLocation())
        }
    }

}