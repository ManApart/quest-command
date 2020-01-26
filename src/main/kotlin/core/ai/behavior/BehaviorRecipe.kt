package core.ai.behavior

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import core.utility.apply

@JsonIgnoreProperties(ignoreUnknown = true)
class BehaviorRecipe(val name: String, val params: Map<String, String> = mapOf()) {
    constructor(base: BehaviorRecipe, params: Map<String, String> = mapOf()) : this(base.name.apply(params), base.params.apply(params))
}