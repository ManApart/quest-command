package core.ai.behavior

@kotlinx.serialization.Serializable
data class BehaviorRecipe(
    val name: String,
    val params: Map<String, String> = mapOf()
)