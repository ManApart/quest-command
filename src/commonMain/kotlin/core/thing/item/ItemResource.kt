package core.thing.item

import core.thing.ThingBuilder

interface ItemResource {
    suspend fun values(): List<ThingBuilder>
}