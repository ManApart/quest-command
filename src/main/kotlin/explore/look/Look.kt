package explore.look

import core.Player
import core.events.EventListener
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing

class Look : EventListener<LookEvent>() {

    override fun execute(event: LookEvent) {
        when {
            event.source.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney(event.source)
            event.thing != null -> describePerceivedThing(event.source, event.thing)
            event.location != null -> describeLocation(event.source, event.location)
            event.source.ai.aggroThing != null -> describeBattle(event.source)
            else -> describeLocation(event.source, event.source.thing.currentLocation())
        }
    }

    private fun describePerceivedThing(source: Player, thing: Thing){
        if (source.thing.perceives(thing)){
            describeThing(source, thing)
        } else {
            source.displayToMe("You're sure ${thing.name} is there, but you're unable to see it.")
        }
    }

}