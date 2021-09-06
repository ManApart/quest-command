package core.body

import traveling.location.location.LocationRecipe
import traveling.location.location.LocationRecipeBuilder

class BodyBuilder(private var name: String = NONE.name) {
    private var part: LocationRecipe? = null
    private var body: Body? = null

    fun body(body: Body) {
        this.body = body
    }

    fun part(name: String? = null, initializer: LocationRecipeBuilder.() -> Unit) {
        part = LocationRecipeBuilder(name ?: this.name).apply(initializer).build()
    }

    internal fun build(base: BodyBuilder? = null): Body {
        if (base != null) {
            if (name == NONE.name) name = base.name
            part = part ?: base.part
            body = body ?: base.body
        }
        return when {
            body != null -> body!!
            name == NONE.name -> NONE
            part == null -> BodyManager.getBody(name)
            else -> Body(name, part!!)
        }
    }

    private fun overrideWith(override: BodyBuilder) {
        if (override.name != NONE.name) name = override.name
        if (override.part != null) part = override.part
        if (override.body != null) body = override.body
    }

    fun build(bases: List<BodyBuilder>) : Body {
        val compiledBase = BodyBuilder()
        bases.forEach { next ->
            compiledBase.overrideWith(next)
        }
        return build(compiledBase)
    }
}

fun body(name: String, initializer: BodyBuilder.() -> Unit = {}): Body {
    return BodyBuilder(name).apply(initializer).build()
}
