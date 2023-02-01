package core.thing.creature

import core.thing.ThingBuilder

interface CreatureResource {
    suspend fun values(): List<ThingBuilder>
}