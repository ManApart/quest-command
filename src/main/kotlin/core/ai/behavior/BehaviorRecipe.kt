package core.ai.behavior

import core.utility.apply

class BehaviorRecipe(val name: String, val params: Map<String, String> = mapOf()) {
    constructor(base: BehaviorRecipe, params: Map<String, String> = mapOf()) : this(base.name.apply(params), base.params.apply(params))
}