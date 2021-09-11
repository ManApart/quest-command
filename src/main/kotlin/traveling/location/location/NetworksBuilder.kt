package traveling.location.location

class NetworksBuilder {
    internal val children = mutableListOf<NetworkBuilder>()

    fun network(item: NetworkBuilder) {
        children.add(item)
    }

    fun network(name: String, initializer: NetworkBuilder.() -> Unit = {}) {
        children.add(NetworkBuilder(name).apply(initializer))
    }
}

fun networks(initializer: NetworksBuilder.() -> Unit): List<NetworkBuilder> {
    return NetworksBuilder().apply(initializer).children
}

fun List<NetworkBuilder>.build(): List<LocationNode> {
    return flatMap { it.build() }
}