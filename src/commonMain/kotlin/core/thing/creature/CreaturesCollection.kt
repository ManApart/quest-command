package core.thing.creature
import core.thing.ThingBuilder

interface CreaturesCollection {
    suspend fun values(): List<ThingBuilder>
}