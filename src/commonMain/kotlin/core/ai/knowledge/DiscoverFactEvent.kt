package core.ai.knowledge

import core.events.Event
import core.properties.ValueKey
import core.properties.props
import core.thing.Thing
import traveling.location.network.LocationNode

data class DiscoverFactEvent(val source: Thing, val fact: Fact) : Event

fun Thing.discover(target: Thing, kind: FactKind) = discover(target, kind.name)
fun Thing.discover(target: Thing, kind: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), kind))
}

fun Thing.setUseTarget(target: Thing, howToUse: HowToUse): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), FactKind.USE_TARGET.name, props(ValueKey.GOAL to howToUse.name)))
}

fun Thing.discover(target: LocationNode, kind: FactKind) = discover(target, kind.name)
fun Thing.discover(target: LocationNode, kind: String): DiscoverFactEvent {
    return DiscoverFactEvent(this, Fact(Subject(target), kind))
}
