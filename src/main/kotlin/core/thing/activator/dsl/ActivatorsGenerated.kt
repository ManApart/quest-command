package core.thing.activator.dsl

class ActivatorsGenerated : ActivatorsCollection {
    override val values by lazy { listOf<ActivatorResource>(resources.thing.activators.CommonActivators()).flatMap { it.values }}
}