package core.thing.activator.dsl

class ActivatorsGenerated : ActivatorsCollection {
    override val values = listOf<ActivatorResource>(resources.thing.activators.CommonActivators()).flatMap { it.values }
}