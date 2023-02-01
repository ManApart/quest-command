package core.thing.activator.dsl
import core.thing.ThingBuilder

interface ActivatorsCollection {
    suspend fun values(): List<ThingBuilder>
}