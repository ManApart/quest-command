package traveling.location.location

class NetworkBuilder(private val networkName: String) {
    private val children = mutableListOf<LocationNodeBuilder>()

    fun locationNode(item: LocationNodeBuilder) {
        children.add(item)
    }

    fun locationNode(name: String? = null, initializer: LocationNodeBuilder.() -> Unit = {}) {
        val nameToUse = name ?: networkName
        children.add(LocationNodeBuilder(nameToUse).apply(initializer))
    }

    fun build(): List<LocationNode> {
        return if (children.isEmpty()){
            listOf(LocationNodeBuilder(networkName).apply { isRoot(true) }.build(networkName))
        } else {
            children.map { it.build(networkName) }
        }

    }
}

fun network(name: String, initializer: NetworkBuilder.() -> Unit = {}): NetworkBuilder {
    return NetworkBuilder(name).apply(initializer)
}