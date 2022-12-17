package explore.examine

import core.events.EventListener
import core.properties.IS_CLIMBING
import explore.look.*

class Examine : EventListener<ExamineEvent>() {

    override fun execute(event: ExamineEvent) {
        when {
            event.source.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney(event.source)
            event.body != null && event.thing != null -> describePerceived(event.source, event.thing) { describeBodyDetailed(event.source, event.thing) }
            event.thing != null -> describePerceived(event.source, event.thing) { describeThingDetailed(event.source, event.thing) }
            event.location != null -> describeLocationDetailed(event.source, event.location)
            event.source.mind.getAggroTarget() != null -> describeBattle(event.source)
            else -> describeLocationDetailed(event.source, event.source.thing.currentLocation())
        }
    }

}