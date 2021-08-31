package core.target.activator.dsl

class ActivatorsGenerated : ActivatorsCollection {
    override val values = listOf<ActivatorResource>().flatMap { it.values }
}