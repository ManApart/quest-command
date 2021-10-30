package explore.examine

import core.Player
import core.events.EventListener
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing
import explore.look.describeBattle
import explore.look.describeClimbJourney
import explore.look.describeLocationDetailed
import explore.look.describeThingDetailed

class Examine : EventListener<ExamineEvent>() {

    override fun execute(event: ExamineEvent) {
        when {
            event.source.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney(event.source)
            event.thing != null -> describePerceivedThing(event.source, event.thing)
            event.location != null -> describeLocationDetailed(event.source, event.location)
            event.source.ai.aggroThing != null -> describeBattle(event.source)
            else -> describeLocationDetailed(event.source, event.source.thing.currentLocation())
        }
    }

    private fun describePerceivedThing(source: Player, thing: Thing){
        if (source.thing.perceives(thing)){
            describeThingDetailed(source, thing)
        } else {
            source.displayToMe("You're sure ${thing.name} is there, but you're unable to see it.")
        }
    }


}