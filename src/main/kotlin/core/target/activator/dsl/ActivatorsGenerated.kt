package core.target.activator.dsl

class ActivatorsGenerated : ActivatorsCollection {
    override val values = listOf<ActivatorResource>(resources.target.activators.CommonActivators()).flatMap { it.values }
}