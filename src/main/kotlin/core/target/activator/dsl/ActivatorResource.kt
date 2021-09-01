package core.target.activator.dsl

import core.target.TargetBuilder

interface ActivatorResource {
    val values: List<TargetBuilder>
}