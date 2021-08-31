package core.target

class TargetsBuilder{
    private val children = mutableListOf<TargetBuilder>()

    fun target(name: String, initializer: TargetBuilder.() -> Unit) {
        children.add(TargetBuilder(name).apply(initializer))
    }

    internal fun build(): List<Target> {
        val builders = children.associateBy { it.name }
        return builders.values.map { buildTarget(it, builders) }.toList()
    }

    private fun buildTarget(builder: TargetBuilder, builders: Map<String, TargetBuilder>): Target {
        val base = builder.baseName?.let { builders[builder.baseName] }
        return builder.build(base)
    }
}

fun targets(initializer: TargetsBuilder.() -> Unit): List<Target> {
    return TargetsBuilder().apply(initializer).build()
}

