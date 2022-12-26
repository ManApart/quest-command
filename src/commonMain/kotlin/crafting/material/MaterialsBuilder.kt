package crafting.material

class MaterialsBuilder {
    private val children = mutableListOf<MaterialBuilder>()
    private val childrenGroups = mutableListOf<MaterialsGroupBuilder>()

    fun material(item: MaterialBuilder) {
        children.add(item)
    }

    fun group(name: String, initializer: MaterialsGroupBuilder.() -> Unit = {}) {
        //Name is just for readability and isn't used
        childrenGroups.add(MaterialsGroupBuilder().apply(initializer))
    }

    fun material(name: String, density: Int = 0, roughness: Int = 0, initializer: MaterialBuilder.() -> Unit = {}) {
        children.add(MaterialBuilder(name).apply {
            this.density = density
            this.roughness = roughness
            initializer()
        })
    }

    fun build(): List<Material> {
        return children.map { it.build(listOf(), listOf()) } + childrenGroups.flatMap { it.build() }
    }
}

fun materials(
    initializer: MaterialsBuilder.() -> Unit
): List<Material> {
    return MaterialsBuilder().apply(initializer).build()
}