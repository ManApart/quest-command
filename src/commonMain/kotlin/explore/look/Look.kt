package explore.look

import core.Player
import core.events.EventListener
import core.history.displayToMe
import core.properties.IS_CLIMBING
import core.thing.Thing

class Look : EventListener<LookEvent>() {

    override suspend fun complete(event: LookEvent) {
        when {
            event.source.properties.values.getBoolean(IS_CLIMBING) -> describeClimbJourney(event.source)
            event.body != null && event.thing != null -> describePerceived(event.source, event.thing) { describeBody(event.source, event.thing) }
            event.part != null && event.thing != null -> describeBodyPart(event.source, event.thing, event.part)
            event.thing != null -> describePerceived(event.source, event.thing) { describeThing(event.source, event.thing) }
            event.source.mind.getAggroTarget() != null -> describeBattle(event.source)
            else -> describeLocation(event.source, event.source.thing.currentLocation())
        }
    }

}

suspend fun describePerceived(source: Player, thing: Thing, describe: suspend () -> Unit) {
    if (source.thing.perceives(thing)) {
        describe()
    } else {
        source.displayToMe("You're sure ${thing.name} is there, but you're unable to see it.")
    }
}
