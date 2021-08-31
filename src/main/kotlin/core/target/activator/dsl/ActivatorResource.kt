package core.target.activator.dsl

import core.target.Target

interface ActivatorResource {
    val values: List<Target>
}