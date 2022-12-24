package crafting.material

class MaterialsBuilder {
    internal val children = mutableListOf<MaterialBuilder>()

    fun material(item: MaterialBuilder) {
        children.add(item)
    }

    fun material(name: String, initializer: MaterialBuilder.() -> Unit) {
        children.add(MaterialBuilder(name).apply(initializer))
    }

    fun build(): List<Material> {
        return children.map { it.build() }
    }
}

fun materials(
    initializer: MaterialsBuilder.() -> Unit
): List<Material> {
    return MaterialsBuilder().apply(initializer).build()
}