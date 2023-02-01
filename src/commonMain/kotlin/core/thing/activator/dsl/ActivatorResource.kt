package core.thing.activator.dsl

import core.thing.ThingBuilder

interface ActivatorResource {
    suspend fun values(): List<ThingBuilder>
}