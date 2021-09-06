package traveling.location.location

class BodyBuilder(private val bodyName: String) {
    private val children = mutableListOf<LocationNodeBuilder>()

    fun location(item: LocationNodeBuilder) {
        children.add(item)
    }

    fun location(name: String? = null, initializer: LocationNodeBuilder.() -> Unit = {}) {
        val nameToUse = name ?: bodyName
        children.add(LocationNodeBuilder(nameToUse).apply(initializer))
    }

    fun build(): List<LocationNode> {
        return children.map { it.build(bodyName) }
    }
}

fun body(name: String, initializer: BodyBuilder.() -> Unit = {}): BodyBuilder {
    return BodyBuilder(name).apply(initializer)
}