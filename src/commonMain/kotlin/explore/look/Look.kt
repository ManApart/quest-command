package explore.look

import core.Player
import core.events.EventListener
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing

class Look : EventListener<LookEvent>() {

    override suspend fun execute(event: LookEvent) {
        when {
            event.source.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney(event.source)
            event.body != null && event.thing != null -> describePerceived(event.source, event.thing) { describeBody(event.source, event.thing) }
            event.thing != null -> describePerceived(event.source, event.thing) { describeThing(event.source, event.thing) }
            event.location != null -> describeLocation(event.source, event.location)
            event.source.mind.getAggroTarget() != null -> describeBattle(event.source)
            else -> describeLocation(event.source, event.source.thing.currentLocation())
        }
    }

}

fun describePerceived(source: Player, thing: Thing, describe: () -> Unit) {
    if (source.thing.perceives(thing)) {
        describe()
    } else {
        source.displayToMe("You're sure ${thing.name} is there, but you're unable to see it.")
    }
}