package traveling.location.location

class BodiesBuilder {
    internal val children = mutableListOf<BodyBuilder>()

    fun body(item: BodyBuilder) {
        children.add(item)
    }

    fun body(name: String, initializer: BodyBuilder.() -> Unit = {}) {
        children.add(BodyBuilder(name).apply(initializer))
    }
}

fun bodies(initializer: BodiesBuilder.() -> Unit): List<BodyBuilder> {
    return BodiesBuilder().apply(initializer).children
}

fun List<BodyBuilder>.build(): List<LocationNode> {
    return flatMap { it.build() }
}