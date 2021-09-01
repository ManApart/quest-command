package core.target.activator.dsl

class ActivatorsGenerated : ActivatorsCollection {
    override val values = listOf<ActivatorResource>(resources.activators.CommonActivators()).flatMap { it.values }
}