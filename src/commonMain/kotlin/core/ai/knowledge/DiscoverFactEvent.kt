package core.ai.knowledge

import core.FactKindStrings
import core.events.Event
import core.properties.Properties
import core.properties.ValueStrings
import core.thing.Thing
import traveling.location.network.LocationNode

data class DiscoverFactEvent(val source: Thing, val fact: Fact) : Event

fun Thing.discover(target: Thing, kind: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), kind))
}

fun Thing.setUseTarget(target: Thing, howToUse: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), FactKindStrings.USE_TARGET, Properties(ValueStrings.GOAL to howToUse)))
}

fun Thing.useTargetGoal(): String? {
    return mind.getUseTarget()?.props?.values?.get(ValueStrings.GOAL)
}

fun Thing.discover(target: LocationNode, kind: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), kind))
}
