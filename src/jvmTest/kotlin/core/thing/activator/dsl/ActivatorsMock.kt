package core.thing.activator.dsl

import core.thing.ThingBuilder

class ActivatorsMock(val values: List<ThingBuilder> = listOf()) : ActivatorsCollection {
    override suspend fun values() = values
}