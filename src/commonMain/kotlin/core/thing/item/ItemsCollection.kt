package core.thing.item
import core.thing.ThingBuilder

interface ItemsCollection {
    suspend fun values(): List<ThingBuilder>
}