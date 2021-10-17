package core.thing.activator.dsl

import core.thing.ThingBuilder

interface ActivatorResource {
    val values: List<ThingBuilder>
}