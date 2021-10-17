package core.thing.activator.dsl
import core.thing.ThingBuilder

interface ActivatorsCollection {
    val values: List<ThingBuilder>
}