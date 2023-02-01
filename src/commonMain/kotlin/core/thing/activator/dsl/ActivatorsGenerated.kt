package core.thing.activator.dsl

class ActivatorsGenerated : ActivatorsCollection {
    override suspend fun values() = listOf<ActivatorResource>(resources.thing.activators.CommonActivators()).flatMap { it.values() }
}