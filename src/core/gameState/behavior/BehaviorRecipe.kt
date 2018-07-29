package core.gameState.behavior

import core.utility.applyParams

class BehaviorRecipe(val name: String, val params: Map<String, String> = mapOf()) {

    fun copy(overrides: Map<String, String>) : BehaviorRecipe {
        return BehaviorRecipe(name, applyParams(params, overrides))
    }
}