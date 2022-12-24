package crafting.material

class MaterialsGenerated : MaterialsCollection {
    override val values by lazy { listOf<MaterialResource>(resources.crafting.material.CommonMaterials()).flatMap { it.values }}
}