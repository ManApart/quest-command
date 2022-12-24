package crafting.material

import magic.Element

class MaterialBuilder(val name: String) {
    var density = 0
    var roughness = 0
    var interaction: (Element) -> Int = { 0 }


    fun build(): Material {
        return Material(name, density, roughness, interaction)
    }
}