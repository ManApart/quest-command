package crafting.material

class MaterialsBuilder {
    internal val children = mutableListOf<MaterialBuilder>()

    fun material(item: MaterialBuilder) {
        children.add(item)
    }

    fun material(name: String, density: Int = 0, roughness: Int = 0, initializer: MaterialBuilder.() -> Unit = {}) {
        children.add(MaterialBuilder(name).apply {
            this.density = density
            this.roughness = roughness
            initializer()
        })
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