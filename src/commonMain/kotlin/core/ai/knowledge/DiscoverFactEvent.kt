package core.ai.knowledge

import core.FactKind
import core.ValueKey
import core.events.Event
import core.properties.Properties
import core.thing.Thing
import traveling.location.network.LocationNode

data class DiscoverFactEvent(val source: Thing, val fact: Fact) : Event

fun Thing.discover(target: Thing, kind: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), kind))
}

fun Thing.setUseTarget(target: Thing, howToUse: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), FactKind.USE_TARGET, Properties(ValueKey.GOAL to howToUse)))
}

fun Thing.useTargetGoal(): String? {
    return mind.getUseTarget()?.props?.values?.get(ValueKey.GOAL)
}

fun Thing.discover(target: LocationNode, kind: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), kind))
}
