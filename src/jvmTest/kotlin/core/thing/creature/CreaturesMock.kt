package core.thing.creature

import core.thing.ThingBuilder

class CreaturesMock(val values: List<ThingBuilder> = listOf()) : CreaturesCollection {
    override suspend fun values() = values
}