package core.body

import traveling.location.location.LocationRecipe
import traveling.location.location.LocationRecipeBuilder

class BodyBuilder(private var name: String = NONE.name) {
    private var part: LocationRecipe? = null

    fun part(name: String? = null, initializer: LocationRecipeBuilder.() -> Unit) {
        part = LocationRecipeBuilder(name ?: this.name).apply(initializer).build()
    }

    internal fun build(): Body {
        return when {
            name == NONE.name -> NONE
            part == null -> BodyManager.getBody(name)
            else -> Body(name, part!!)
        }
    }

}

fun body(name: String, initializer: BodyBuilder.() -> Unit = {}): Body {
    return BodyBuilder(name).apply(initializer).build()
}