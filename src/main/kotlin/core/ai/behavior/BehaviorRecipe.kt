package core.ai.behavior

data class BehaviorRecipe(
        val name: String,
        val params: Map<String, String> = mapOf()
)