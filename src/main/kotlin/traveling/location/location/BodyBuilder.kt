package traveling.location.location

class BodyBuilder(private val bodyName: String) {
    private val children = mutableListOf<LocationNodeBuilder>()

    fun location(item: LocationNodeBuilder) {
        children.add(item)
    }

    fun location(name: String, initializer: LocationNodeBuilder.() -> Unit = {}) {
        children.add(LocationNodeBuilder(name).apply(initializer))
    }

    fun build(): List<LocationNode> {
        return children.map { it.build(bodyName) }
    }
}